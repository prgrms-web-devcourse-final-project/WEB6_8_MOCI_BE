package com.moci_3d_backend.domain.archive.archive_request.service;

import com.moci_3d_backend.domain.archive.archive_request.dto.*;
import com.moci_3d_backend.domain.archive.archive_request.entity.ArchiveRequest;
import com.moci_3d_backend.domain.archive.archive_request.entity.RequestCategory;
import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import com.moci_3d_backend.domain.archive.archive_request.mapper.ArchiveRequestMapper;
import com.moci_3d_backend.domain.archive.archive_request.repository.ArchiveRequestRepository;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.repository.UserRepository;
import com.moci_3d_backend.global.validator.AuthValidator;
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
    private final AuthValidator authValidator;

    // 자료 요청글 생성 (멘토용)
    @Transactional
    public ArchiveRequestResponseDto createArchiveRequest(ArchiveRequestCreateDto createDto, User actor) {
        authValidator.validateMentor(actor);

        ArchiveRequest archiveRequest = ArchiveRequest.builder()
                .user(actor)
                .title(createDto.getTitle())
                .description(createDto.getDescription())
                .category(createDto.getCategory())
                .status(RequestStatus.PENDING)
                .build();

        ArchiveRequest savedRequest = archiveRequestRepository.save(archiveRequest);
        return archiveRequestMapper.toResponseDto(savedRequest);
    }

    // 자료 요청 목록 조회 (페이징, 관리자, 멘토)
    @Transactional(readOnly = true)
    public ArchiveRequestListResponseDto getArchiveRequests(Pageable pageable, User actor) {
        authValidator.validateMentorOrAdmin(actor);

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
    public ArchiveRequestResponseDto getArchiveRequest(Long requestId, User actor) {
        authValidator.validateMentorOrAdmin(actor);

        ArchiveRequest archiveRequest = archiveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId));

        return archiveRequestMapper.toResponseDto(archiveRequest);
    }

    // 자료 요청 수정 (제목, 설명, 카테고리)
    @Transactional
    public ArchiveRequestResponseDto updateArchiveRequestWithOwnerCheck(Long requestId, ArchiveRequestUpdateDto updateDto, User actor) {
        authValidator.validateMentor(actor);

        ArchiveRequest archiveRequest = archiveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId));

        authValidator.validateOwner(archiveRequest.getUser(), actor);

        // REJECTED 상태인 경우 수정 시 PENDING으로 변경
        if (archiveRequest.isRejected()) {
            archiveRequest.setStatus(RequestStatus.PENDING);
            archiveRequest.setReviewedBy(null);
        } else if (!archiveRequest.isPending()) {
            throw new IllegalStateException("승인된 요청은 수정할 수 없습니다.");
        }

        archiveRequest.setTitle(updateDto.getTitle());
        archiveRequest.setDescription(updateDto.getDescription());
        archiveRequest.setCategory(updateDto.getCategory());

        return archiveRequestMapper.toResponseDto(archiveRequest);
    }

    // 자료 요청 상태 변경 (관리자용)
    @Transactional
    public ArchiveRequestResponseDto updateArchiveRequestStatus(
            Long requestId, ArchiveRequestStatusUpdateDto statusUpdateDto, User actor) {
        authValidator.validateAdmin(actor);

        ArchiveRequest archiveRequest = archiveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId));

        RequestStatus newStatus = statusUpdateDto.getStatus();

        // 상태 변경 로직
        switch (newStatus) {
            case APPROVED:
                archiveRequest.approve(actor);
                break;
            case REJECTED:
                archiveRequest.reject(actor);
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
    public void deleteArchiveRequestWithPermissionCheck(Long requestId, User actor) {
        authValidator.validateMentorOrAdmin(actor);

        ArchiveRequest archiveRequest = archiveRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id의 자료 요청을 찾을 수 없습니다: " + requestId));

        if (authValidator.isAdmin(actor)) {
            archiveRequestRepository.deleteById(requestId);
            return;
        }

        // 멘토의 경우: 본인 요청만 삭제 가능 (PENDING 또는 REJECTED 상태)
        authValidator.validateOwner(archiveRequest.getUser(), actor);
        
        if (archiveRequest.isApproved()) {
            throw new IllegalStateException("승인된 요청은 삭제할 수 없습니다.");
        }
        
        archiveRequestRepository.deleteById(requestId);
    }


    // 사용자별 자료 요청 목록 조회
    @Transactional(readOnly = true)
    public ArchiveRequestListResponseDto getArchiveRequestsByUser(Long userId, Pageable pageable, User actor) {
        authValidator.validateMentorOrAdmin(actor);

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
    public ArchiveRequestListResponseDto getArchiveRequestsByStatus(RequestStatus status, Pageable pageable, User actor) {
        authValidator.validateMentorOrAdmin(actor);

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
    public long getPendingRequestCount(User actor) {
        authValidator.validateAdmin(actor);

        return archiveRequestRepository.countByStatus(RequestStatus.PENDING);
    }

    // 카테고리별 자료 요청 목록 조회
    @Transactional(readOnly = true)
    public ArchiveRequestListResponseDto getArchiveRequestsByCategory(RequestCategory category, Pageable pageable, User actor) {
        authValidator.validateMentorOrAdmin(actor);

        Page<ArchiveRequest> requestPage = archiveRequestRepository.findByCategory(category, pageable);

        Page<ArchiveRequestListResponseDto.RequestSummaryDto> summaryPage =
                requestPage.map(archiveRequestMapper::toSummaryDto);

        return ArchiveRequestListResponseDto.builder()
                .totalPages(summaryPage.getTotalPages())
                .totalElements(summaryPage.getTotalElements())
                .currentPage(summaryPage.getNumber())
                .requests(summaryPage.getContent())
                .build();
    }

    // 상태와 카테고리별 자료 요청 목록 조회
    @Transactional(readOnly = true)
    public ArchiveRequestListResponseDto getArchiveRequestsByStatusAndCategory(
            RequestStatus status, RequestCategory category, Pageable pageable, User actor) {
        authValidator.validateMentorOrAdmin(actor);

        Page<ArchiveRequest> requestPage = archiveRequestRepository.findByStatusAndCategory(status, category, pageable);

        Page<ArchiveRequestListResponseDto.RequestSummaryDto> summaryPage =
                requestPage.map(archiveRequestMapper::toSummaryDto);

        return ArchiveRequestListResponseDto.builder()
                .totalPages(summaryPage.getTotalPages())
                .totalElements(summaryPage.getTotalElements())
                .currentPage(summaryPage.getNumber())
                .requests(summaryPage.getContent())
                .build();
    }

    // 사용자별, 카테고리별 자료 요청 목록 조회
    @Transactional(readOnly = true)
    public ArchiveRequestListResponseDto getArchiveRequestsByUserAndCategory(
            Long userId, RequestCategory category, Pageable pageable, User actor) {
        authValidator.validateMentorOrAdmin(actor);

        Page<ArchiveRequest> requestPage = archiveRequestRepository.findByUserIdAndCategory(userId, category, pageable);

        Page<ArchiveRequestListResponseDto.RequestSummaryDto> summaryPage =
                requestPage.map(archiveRequestMapper::toSummaryDto);

        return ArchiveRequestListResponseDto.builder()
                .totalPages(summaryPage.getTotalPages())
                .totalElements(summaryPage.getTotalElements())
                .currentPage(summaryPage.getNumber())
                .requests(summaryPage.getContent())
                .build();
    }

    // 카테고리별 요청 개수 조회
    @Transactional(readOnly = true)
    public long getRequestCountByCategory(RequestCategory category, User actor) {
        authValidator.validateMentorOrAdmin(actor);

        return archiveRequestRepository.countByCategory(category);
    }
}
