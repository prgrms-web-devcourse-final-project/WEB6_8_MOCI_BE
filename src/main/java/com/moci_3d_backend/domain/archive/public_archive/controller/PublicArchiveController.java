package com.moci_3d_backend.domain.archive.public_archive.controller;

import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveCreateRequest;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveListResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveUpdateRequest;
import com.moci_3d_backend.domain.archive.public_archive.service.PublicArchiveService;
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
@Tag(name = "교육 자료실", description = "교육 자료실 관련 API (현재 인증인가 미구현으로 **수동으로 userId를 넣어야 합니다**(구현시 수정예정)")
public class PublicArchiveController {

    private final PublicArchiveService publicArchiveService;

    // ==================================
    // =  Public API(인증 필요 x)  =
    // ==================================

    // 교육 자료실 목록 조회 (페이징)
    @GetMapping("/archive/public")
    @Operation(summary = "교육 자료실 목록 조회", description = "모든 사용자가 교육자료실 목록을 조회할 수 있습니다. (페이징)\n\n" +
       "** 검색 기능: **\n" +
    "- keyword 파라미터를 추가하면 title과 description에서 검색합니다. \n" +
    "- keyword가 없으면 전체 목록을 반환합니다. (하위 호환성 유지)")
    public RsData<PublicArchiveListResponse> getPublicArchives(
            @RequestParam(required = false)
            @Parameter(
                    description = "검색 키워드 (선택사항, title 또는 description에서 검색)",
                    example = "카카오톡"
            ) String keyword,

            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            @Parameter(
                    description = "페이징 정보 (기본: 10개씩, 최신순)",
                    example = "{\n  \"page\": 0,\n  \"size\": 10,\n  \"sort\": \"createdAt\"\n}"
            ) Pageable pageable
    ) {
        // 키워드가 있으면 검색, 없으면 전체 조회 (하위 호환성 유지)
        PublicArchiveListResponse response;
        if (keyword != null && !keyword.trim().isEmpty()) {
            response = publicArchiveService.searchPublicArchives(keyword, pageable);
        } else {
            response = publicArchiveService.getPublicArchives(pageable);
        }

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
    @Operation(summary = "[관리자] 교육 자료실 등록", description = "관리자만 교육자료실에 글을 등록할 수 있습니다.\n" +
            "현재 플로우: 프론트에서 텍스트 및 파일**(선택사항 & 파일 첨부시 파일 업로드API먼저 호출 필요)** -> 양식에 맞게 본 API호출 -> DB저장")
    public RsData<PublicArchiveResponse> createPublicArchive(
            @Valid @RequestBody PublicArchiveCreateRequest request,
            // TODO 임시: 테스트용 userId 파라미터 (추후 @AuthenticationPrincipal 또는 SecurityContextHolder로 대체)
            @RequestParam @Parameter(description = "임시 테스트용 관리자 ID") Long userId
            ) {
        PublicArchiveResponse response = publicArchiveService.createPublicArchive(request, userId);
        return RsData.of(201, "success to create public archive", response);
    }

    @PutMapping("/admin/archive/public/{archiveId}")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    @Operation(summary = "[관리자] 교육 자료실 수정", description = "관리자만 교육자료실 글을 수정할 수 있습니다.")
    public RsData<PublicArchiveResponse> updatePublicArchive(
            @PathVariable @Parameter(description = "수정할 자료실 ID") Long archiveId,
            @Valid @RequestBody PublicArchiveUpdateRequest request,
            // 임시: 테스트용 userId 파라미터 (추후 본인 작성 글 확인용으로 사용)
            @RequestParam @Parameter(description = "임시 테스트용 관리자 ID", example = "1") Long userId
            ) {
        PublicArchiveResponse response = publicArchiveService.updatePublicArchive(archiveId, request, userId);
        return RsData.of(200, "success to update public archive", response);
    }

    @DeleteMapping("/admin/archive/public/{archiveId}")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    @Operation(summary = "[관리자] 교육 자료실 삭제", description = "관리자만 교육자료실 글을 삭제할 수 있습니다.")
    public RsData<Void> deletePublicArchive
            (@PathVariable @Parameter(description = "삭제할 자료실 ID") Long archiveId,
             // 임시: 테스트용 userId 파라미터 (추후 본인 작성 글 확인용으로 사용)
             @RequestParam @Parameter(description = "임시 테스트용 관리자 ID", example = "1") Long userId) {
        publicArchiveService.deletePublicArchive(archiveId, userId);
        return RsData.of(200, "success to delete public archive");
    }
}
