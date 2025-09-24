package com.moci_3d_backend.domain.archive.archive_request.service;

import com.moci_3d_backend.domain.archive.archive_request.dto.*;
import com.moci_3d_backend.domain.archive.archive_request.entity.ArchiveRequest;
import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import com.moci_3d_backend.domain.archive.archive_request.mapper.ArchiveRequestMapper;
import com.moci_3d_backend.domain.archive.archive_request.repository.ArchiveRequestRepository;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArchiveRequestService {

    private final ArchiveRequestRepository archiveRequestRepository;
    private final ArchiveRequestMapper archiveRequestMapper;
    private final UserRepository userRepository;

    // 자료 요청글 생성 (멘토용)
    @Transactional
    public ArchiveRequestResponseDto createArchiveRequest(ArchiveRequestCreateDto createDto, Long userId) {

         User user = userRepository.findById(userId)
                 .orElseThrow(() -> new EntityNotFoundException("해당 id의 유저를 찾을 수 없습니다: " + userId));

        ArchiveRequest archiveRequest = ArchiveRequest.builder()
                .user(user)
                .title(createDto.getTitle())
                .description(createDto.getDescription())
                .status(RequestStatus.PENDING)
                .build();

        ArchiveRequest savedRequest = archiveRequestRepository.save(archiveRequest);
        return archiveRequestMapper.toResponseDto(savedRequest);
    }

    // 자료 요청 목록 조회 (페이징)
    @Transactional(readOnly = true)
    public ArchiveRequestListResponseDto getArchiveRequests(Pageable pageable) {
        Page<ArchiveRequest> requestPage = archiveRequestRepository.findAll(pageable);

        Page<ArchiveRequestListResponseDto.RequestSummaryDto> summaryPage =
                requestPage.map(archiveRequestMapper::toSummaryDto);

        return ArchiveRequestListResponseDto.builder()
                .totalPages(summaryPage.getTotalPages())
                .totalElements(summaryPage.getTotalElements())
                .currentPage(summaryPage.getNumber())
                .requests(summaryPage.getContent())
                .build();
    }

    // 자료 요청 상세 조회
    @Transactional(readOnly = true)
    public ArchiveRequestResponseDto getArchiveRequest(Long requestId) {
        ArchiveRequest archiveRequest = archiveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId));

        return archiveRequestMapper.toResponseDto(archiveRequest);
    }

    // 자료 요청 수정 (제목, 설명)
    @Transactional
    public ArchiveRequestResponseDto updateArchiveRequestWithOwnerCheck(Long requestId, ArchiveRequestUpdateDto updateDto, Long userId) {
        ArchiveRequest archiveRequest = archiveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId));

        if (!isOwner(requestId, userId)) {
            throw new IllegalStateException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        if (!archiveRequest.isPending()) {
            throw new IllegalStateException("완료된 요청은 수정할 수 없습니다.");
        }

        // 수정 가능한 상태인지 확인(대기중일때만)
        if (!archiveRequest.isPending()) {
            throw new IllegalStateException("완료된 요청은 수정할 수 없습니다.");
        }

        archiveRequest.setTitle(updateDto.getTitle());
        archiveRequest.setDescription(updateDto.getDescription());

        return archiveRequestMapper.toResponseDto(archiveRequest);
    }

    // 자료 요청 상태 변경 (관리자용)
    @Transactional
    public ArchiveRequestResponseDto updateArchiveRequestStatus(
            Long requestId, ArchiveRequestStatusUpdateDto statusUpdateDto, Long reviewerId) {
        ArchiveRequest archiveRequest = archiveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId));

         User reviewer = userRepository.findById(reviewerId)
                 .orElseThrow(() -> new EntityNotFoundException("해당 id의 검토자를 찾을 수 없습니다: " + reviewerId));

        RequestStatus newStatus = statusUpdateDto.getStatus();

        // 상태 변경 로직
        switch (newStatus) {
            case APPROVED:
                archiveRequest.approve(reviewer);
                break;
            case REJECTED:
                archiveRequest.reject(reviewer);
                break;
            case PENDING:
                archiveRequest.setStatus(RequestStatus.PENDING);
                archiveRequest.setReviewedBy(null);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 상태입니다: " + newStatus);
        }
        return archiveRequestMapper.toResponseDto(archiveRequest);
    }

    // 자료 요청 삭제
    @Transactional
    public void deleteArchiveRequestWithPermissionCheck(Long requestId, Long userId) {
        if (!archiveRequestRepository.existsById(requestId)) {
            throw new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId);
        }

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new EntityNotFoundException("해당 id의 유저를 찾을 수 없습니다: " + userId));

        if (user.getRole() == User.UserRole.ADMIN) {
            // 관리자는 모든 요청글 삭제 가능
            archiveRequestRepository.deleteById(requestId);
            return;
        }

        if (user.getRole() == User.UserRole.MENTOR && isOwner(requestId, userId)) {
            // 멘토는 본인이 작성한 요청글만 삭제 가능
            archiveRequestRepository.deleteById(requestId);
            return;
        }

        throw new IllegalStateException("삭제 권한이 없습니다. (관리자 또는 본인 작성 글만 가능)");
    }


    // 사용자별 자료 요청 목록 조회
    @Transactional(readOnly = true)
    public ArchiveRequestListResponseDto getArchiveRequestsByUser(Long userId, Pageable pageable) {
        Page<ArchiveRequest> requestpage = archiveRequestRepository.findByUserId(userId, pageable);

        Page<ArchiveRequestListResponseDto.RequestSummaryDto> summaryPage =
                requestpage.map(archiveRequestMapper::toSummaryDto);

        return ArchiveRequestListResponseDto.builder()
                .totalPages(summaryPage.getTotalPages())
                .totalElements(summaryPage.getTotalElements())
                .currentPage(summaryPage.getNumber())
                .requests(summaryPage.getContent())
                .build();
    }

    // 상태별 자료 요청 목록 조회
    @Transactional(readOnly = true)
    public ArchiveRequestListResponseDto getArchiveRequestsByStatus(RequestStatus status, Pageable pageable) {
        Page<ArchiveRequest> requestPage = archiveRequestRepository.findByStatus(status, pageable);

        Page<ArchiveRequestListResponseDto.RequestSummaryDto> summaryPage =
                requestPage.map(archiveRequestMapper::toSummaryDto);

        return ArchiveRequestListResponseDto.builder()
                .totalPages(summaryPage.getTotalPages())
                .totalElements(summaryPage.getTotalElements())
                .currentPage(summaryPage.getNumber())
                .requests(summaryPage.getContent())
                .build();
    }

    // 대기중 요청 개수 조회
    @Transactional(readOnly = true)
    public long getPendingRequestCount() {
        return archiveRequestRepository.countByStatus(RequestStatus.PENDING);
    }

    // 본인 작성 글 여부 검증
    @Transactional(readOnly = true)
    public boolean isOwner(Long requestId, Long userId) {
        ArchiveRequest archiveRequest = archiveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId));
        return archiveRequest.getUser().getId().equals(userId);
    }

    // 관리자 여부 검증
    @Transactional(readOnly = true)
    public boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 유저를 찾을 수 없습니다: " + userId));
        return user.getRole() == User.UserRole.ADMIN;
    }
}
