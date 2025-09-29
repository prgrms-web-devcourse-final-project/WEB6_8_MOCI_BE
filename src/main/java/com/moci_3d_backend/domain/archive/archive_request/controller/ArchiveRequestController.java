package com.moci_3d_backend.domain.archive.archive_request.controller;

import com.moci_3d_backend.domain.archive.archive_request.dto.*;
import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import com.moci_3d_backend.domain.archive.archive_request.service.ArchiveRequestService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "자료 요청 게시판", description = "자료 요청 게시판 관련 API 입니다.")
public class ArchiveRequestController {
    private final ArchiveRequestService archiveRequestService;
    private final Rq rq;

    // === Mentor API (멘토 권한 필요) ===

    // 자료 요청글 생성 (멘토)
    @PostMapping("/archive-requests")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 자료 요청 등록", description = "멘토가 자료 요청글을 등록합니다.")
    public RsData<ArchiveRequestResponseDto> createArchiveRequest(
            @Valid @RequestBody ArchiveRequestCreateDto createDto
    ) {
        User actor = rq.getActor();
        ArchiveRequestResponseDto response = archiveRequestService.createArchiveRequest(createDto, actor);
        return RsData.of(201, "자료 요청글이 생성되었습니다.", response);
    }

    // 자료 요청 수정 (멘토)
    @PutMapping("/archive-requests/{requestId}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 자료 요청 수정", description = "멘토가 본인의 요청글을 수정합니다.")
    public RsData<ArchiveRequestResponseDto> updateArchiveRequest(
            @PathVariable @Parameter(description = "수정할 요청글 ID", example = "1") Long requestId,
            @Valid @RequestBody ArchiveRequestUpdateDto updateDto
    ) {
        User actor = rq.getActor();
        ArchiveRequestResponseDto response = archiveRequestService.updateArchiveRequestWithOwnerCheck(requestId, updateDto, actor);
        return RsData.of(200, "자료 요청글이 수정되었습니다.", response);
    }

    // 자료 요청 삭제 (멘토 + 관리자)
    @DeleteMapping("/archive-requests/{requestId}")
    @PreAuthorize("hasRole('MENTOR') or hasRole('ADMIN')")
    @Operation(summary = "[멘토/관리자] 자료 요청 삭제", description = "멘토가 본인의 글을, 관리자는 모든 글에대해 삭제할 권한이 있습니다.")
    public RsData<Void> deleteArchiveRequest(
            @PathVariable @Parameter(description = "삭제할 요청글 ID", example = "1") Long requestId
    ) {
        User actor = rq.getActor();
        archiveRequestService.deleteArchiveRequestWithPermissionCheck(requestId, actor);
        return RsData.of(200, "자료 요청글이 성공적으로 삭제되었습니다.");
    }


    // === Admin & Mentor API ===

    // 자료 요청 목록 조회 (관리자, 멘토)
    @GetMapping("/archive-requests")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    @Operation(summary = "[관리자/멘토] 자료 요청 목록 조회", description = "관리자와 멘토가 자료 요청 목록을 조회할 수 있습니다.")
    public RsData<ArchiveRequestListResponseDto> getArchiveRequests(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            @Parameter(description = "페이징 정보 (기본: 10개씩, 최신순)",
                    example = "{\n  \"page\": 0,\n  \"size\": 10,\n  \"sort\": \"createdAt\"\n}"
            ) Pageable pageable,
            @RequestParam(required = false) @Parameter(description = "상태 필터 (PENDING, APPROVED, REJECTED)") RequestStatus status) {
        ArchiveRequestListResponseDto response;
        User actor = rq.getActor();

        if (status != null) {
            response = archiveRequestService.getArchiveRequestsByStatus(status, pageable, actor);
        } else {
            response = archiveRequestService.getArchiveRequests(pageable, actor);
        }

        return RsData.of(200, "자료 요청 목록을 성공적으로 조회했습니다.", response);
    }

    // 자료 요청 상세 조회 (관리자, 멘토)
    @GetMapping("/archive-requests/{requestId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    @Operation(summary = "[관리자/멘토] 자료 요청 상세 조회", description = "관리자와 멘토가 자료 요청 상세 정보를 조회할 수 있습니다.")
    public RsData<ArchiveRequestResponseDto> getArchiveRequest(
            @PathVariable @Parameter(description = "조회할 요청글 ID") Long requestId
    ) {
        User actor = rq.getActor();
        ArchiveRequestResponseDto response = archiveRequestService.getArchiveRequest(requestId, actor);
        return RsData.of(200, "자료 요청 상세 정보를 성공적으로 조회했습니다.", response);
    }

    // 사용자별 자료 요청 목록 조회 (관리자, 멘토)
    @GetMapping("/users/{userId}/archive-requests")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MENTOR')")
    @Operation(summary = "[관리자/멘토] 사용자별(멘토) 자료 요청 목록 조회", description = "관리자와 멘토가 특정 사용자(멘토)의 자료 요청 목록을 조회할 수 있습니다.")
    public RsData<ArchiveRequestListResponseDto> getUserArchiveRequestByUser(
            @PathVariable @Parameter(description = "조회할 멘토 ID", example = "2") Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            @Parameter(description = "페이징 정보 (기본: 10개씩, 최신순)", example = "{\n  \"page\": 0,\n  \"size\": 10,\n  \"sort\": \"createdAt\"\n}"
            ) Pageable pageable
    ) {
        User actor = rq.getActor();
        ArchiveRequestListResponseDto response = archiveRequestService.getArchiveRequestsByUser(userId, pageable, actor);
        return RsData.of(200, "사용자별 자료 요청 목록을 성공적으로 조회했습니다.", response);
    }


    // === Admin API (관리자 권한 필요) ===

    // 자료 요청 상태 변경 (관리자)
    @PatchMapping("/archive-requests/{requestId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자] 자료 요청 상태 변경", description = "관리자가 자료 요청글의 상태를 변경합니다.")
    public RsData<ArchiveRequestResponseDto> updateArchiveRequestStatus(
            @PathVariable @Parameter(description = "상태를 변경할 요청글 ID", example = "1") Long requestId,
            @Valid @RequestBody ArchiveRequestStatusUpdateDto statusUpdateDto
    ) {
        User actor = rq.getActor();
        ArchiveRequestResponseDto response = archiveRequestService.updateArchiveRequestStatus(requestId, statusUpdateDto, actor);

        String statusMessage = switch (statusUpdateDto.getStatus()) {
            case APPROVED -> "승인";
            case REJECTED -> "거절";
            case PENDING -> "대기중으로 변경";
        };

        return RsData.of(200, String.format("자료 요청이 성공적으로 %s 되었습니다.", statusMessage), response);
    }

    // 대기중인 요청 개수 조회 (관리자)
    @GetMapping("/archive-requests/pending/count")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자] 대기중인 자료 요청 개수 조회", description = "관리자가 대기중인 자료 요청글의 개수를 조회합니다.")
    public RsData<Long> getPendingRequestCount(
    ) {
        User actor = rq.getActor();
        long count = archiveRequestService.getPendingRequestCount(actor);
        return RsData.of(200, "대기중인 자료 요청 개수를 성공적으로 조회했습니다.", count);
    }
}
