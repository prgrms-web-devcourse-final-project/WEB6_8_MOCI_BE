package com.moci_3d_backend.domain.archive.public_archive.controller;

import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveCreateRequest;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveListResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveUpdateRequest;
import com.moci_3d_backend.domain.archive.public_archive.service.PublicArchiveService;
import com.moci_3d_backend.domain.user.entity.User;
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
@Tag(name = "교육 자료실", description = "교육 자료실 관련 API")
public class PublicArchiveController {

    private final PublicArchiveService publicArchiveService;

    // ==================================
    // =  Public API(인증 필요 x)  =
    // ==================================

    // 교육 자료실 목록 조회 (페이징)
    @GetMapping("/archive/public")
    @Operation(summary = "교육 자료실 목록 조회", description = "모든 사용자가 교육자료실 목록을 조회할 수 있습니다. (페이징)")
    public RsData<PublicArchiveListResponse> getPublicArchives(
            @PageableDefault(size = 10, sort = "uploadedAt", direction = Sort.Direction.DESC)
            @Parameter(description = "페이징 정보 (기본: 10개씩, 최신순)") Pageable pageable
    ) {
        PublicArchiveListResponse response = publicArchiveService.getPublicArchives(pageable);
        return RsData.of(200, "success to get public archives", response);
    }

    // 교육 자료실 상세 조회
    @GetMapping("/archive/public/{archiveId}")
    @Operation(summary = "교육 자료실 상세 조회", description = "모든 사용자가 교육자료실 상세 정보를 조회할 수 있습니다.")
    public RsData<PublicArchiveResponse> getPublicArchive(
            @PathVariable @Parameter(description = "자료실 ID") Long archiveId
    ) {
        PublicArchiveResponse response = publicArchiveService.getPublicArchive(archiveId);
        return RsData.of(200, "success to get public archive", response);
    }


    // ==================================
    // =  Admin API(인증 필요 o)  =
    // ==================================

    @PostMapping("/admin/archive/public")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    @Operation(summary = "[관리자] 교육 자료실 등록", description = "관리자만 교육자료실에 글을 등록할 수 있습니다.")
    public RsData<PublicArchiveResponse> createPublicArchive(
            @Valid @RequestBody PublicArchiveCreateRequest request,
            // TODO: 실제 구현시 @AuthenticationPrincipal 또는 SecurityContext에서 User 정보를 가져와야 함
            User user // 컴파일 에러 방지용
            ) {
        PublicArchiveResponse response = publicArchiveService.createPublicArchive(request, user.getId());
        return RsData.of(201, "success to create public archive", response);
    }

    @PutMapping("/admin/archive/public/{archiveId}")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    @Operation(summary = "[관리자] 교육 자료실 수정", description = "관리자만 교육자료실 글을 수정할 수 있습니다.")
    public RsData<PublicArchiveResponse> updatePublicArchive(
            @PathVariable @Parameter(description = "수정할 자료실 ID") Long archiveId,
            @Valid @RequestBody PublicArchiveUpdateRequest request
            ) {
        PublicArchiveResponse response = publicArchiveService.updatePublicArchive(archiveId, request);
        return RsData.of(200, "success to update public archive", response);
    }

    @DeleteMapping("/admin/archive/public/{archiveId}")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    @Operation(summary = "[관리자] 교육 자료실 삭제", description = "관리자만 교육자료실 글을 삭제할 수 있습니다.")
    public RsData<Void> deletePublicArchive
            (@PathVariable @Parameter(description = "삭제할 자료실 ID") Long archiveId) {
        publicArchiveService.deletePublicArchive(archiveId);
        return RsData.of(200, "success to delete public archive");
    }
}
