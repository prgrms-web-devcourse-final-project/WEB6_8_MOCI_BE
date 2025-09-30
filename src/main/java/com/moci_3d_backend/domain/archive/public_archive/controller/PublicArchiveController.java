package com.moci_3d_backend.domain.archive.public_archive.controller;

import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveCreateRequest;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveListResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveUpdateRequest;
import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
import com.moci_3d_backend.domain.archive.public_archive.service.PublicArchiveService;
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
@Tag(name = "교육 자료실", description = "교육 자료실 관련 API 입니다.")
public class PublicArchiveController {

    private final PublicArchiveService publicArchiveService;
    private final Rq rq;

    // ==================================
    // =  Public API(인증 필요 x)  =
    // ==================================

    // 교육 자료실 목록 조회 (페이징 + 검색 + 카테고리 필터링)
    @GetMapping("/archive/public")
    @Operation(summary = "교육 자료실 목록 조회", description = "모든 사용자가 교육자료실 목록을 조회할 수 있습니다. (페이징)\n\n" +
            "** 검색 기능: **\n" +
            "- category 파라미터로 필터링\n" +
            "- keyword 파라미터로 제목/내용 검색\n" +
            "- 두 파라미터를 함께 사용하면 해당 카테고리 내에서 검색\n" +
            "- 파라미터가 없으면 전체 목록을 반환합니다.")
    public RsData<PublicArchiveListResponse> getPublicArchives(
            @RequestParam(required = false)
            @Parameter(
                    description = "카테고리 필터",
            example = "KAKAO_TALK") ArchiveCategory category,

            @RequestParam(required = false)
            @Parameter(
                    description = "검색 키워드 (선택사항, title 또는 description에서 검색)",
                    example = "친구에게 선물 보내는 방법"
            ) String keyword,

            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            @Parameter(
                    description = "페이징 정보 (기본: 10개씩, 최신순)",
                    example = "{\n  \"page\": 0,\n  \"size\": 10,\n  \"sort\": \"createdAt\"\n}"
            ) Pageable pageable
    ) {
        PublicArchiveListResponse response;

        if (category != null && keyword != null && !keyword.trim().isEmpty()) {
            // 1. 카테고리 + 키워드 검색
            response = publicArchiveService.searchByKeywordAndCategory(keyword, category, pageable);
        } else if (category != null) {
            // 2. 카테고리만 필터링
            response = publicArchiveService.getPublicArchivesByCategory(category, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            // 3. 키워드만 검색 (전체 카테고리)
            response = publicArchiveService.searchPublicArchives(keyword, pageable);
        } else {
            // 4. 전체 조회
            response = publicArchiveService.getPublicArchives(pageable);
        }

        return RsData.of(200, "교육자료실 목록 조회 성공", response);
    }

    // 교육 자료실 상세 조회
    @GetMapping("/archive/public/{archiveId}")
    @Operation(summary = "교육 자료실 상세 조회", description = "모든 사용자가 교육자료실 상세 정보를 조회할 수 있습니다.")
    public RsData<PublicArchiveResponse> getPublicArchive(
            @PathVariable @Parameter(description = "자료실 ID") Long archiveId
    ) {
        PublicArchiveResponse response = publicArchiveService.getPublicArchive(archiveId);
        return RsData.of(200, "교육 자료실 상세조회 성공", response);
    }


    // ==================================
    // =  Admin API(인증 필요 o)  =
    // ==================================

    @PostMapping("/admin/archive/public")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    @Operation(summary = "[관리자] 교육 자료실 등록", description = "관리자만 교육자료실에 글을 등록할 수 있습니다.\n" +
            "현재 플로우: 프론트에서 텍스트 및 파일**(선택사항 & 파일 첨부시 파일 업로드API먼저 호출 필요)** -> 양식에 맞게 본 API호출 -> DB저장")
    public RsData<PublicArchiveResponse> createPublicArchive(
            @Valid @RequestBody PublicArchiveCreateRequest request
    ) {
        User actor = rq.getActor();
        PublicArchiveResponse response = publicArchiveService.createPublicArchive(request, actor);
        return RsData.of(201, "교육 자료실 글 생성 성공 ", response);
    }

    @PutMapping("/admin/archive/public/{archiveId}")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    @Operation(summary = "[관리자] 교육 자료실 수정", description = "관리자만 교육자료실 글을 수정할 수 있습니다.")
    public RsData<PublicArchiveResponse> updatePublicArchive(
            @PathVariable @Parameter(description = "수정할 자료실 ID") Long archiveId,
            @Valid @RequestBody PublicArchiveUpdateRequest request
    ) {
        User actor = rq.getActor();
        PublicArchiveResponse response = publicArchiveService.updatePublicArchive(archiveId, request, actor);
        return RsData.of(200, "교육 자료실 글 수정 성공", response);
    }

    @DeleteMapping("/admin/archive/public/{archiveId}")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 필요
    @Operation(summary = "[관리자] 교육 자료실 삭제", description = "관리자만 교육자료실 글을 삭제할 수 있습니다.")
    public RsData<Void> deletePublicArchive
            (@PathVariable @Parameter(description = "삭제할 자료실 ID") Long archiveId) {
        User actor = rq.getActor();
        publicArchiveService.deletePublicArchive(archiveId, actor);
        return RsData.of(200, "교육 자료실 글 삭제 성공");
    }
}
