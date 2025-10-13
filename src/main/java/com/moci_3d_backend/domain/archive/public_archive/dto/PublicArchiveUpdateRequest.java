package com.moci_3d_backend.domain.archive.public_archive.dto;

import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PublicArchiveUpdateRequest {
    private final String title;
    private final String description;
    private final ArchiveCategory category;
    private final String subCategory;

    @Schema(
            description = "업데이트할 파일 ID 목록 (선택사항)\n" +
                    "- null: 파일 변경하지 않음 (기존 파일 유지)\n" +
                    "- 빈 배열 []: 모든 파일 삭제\n" +
                    "- [1, 2, 3]: 기존 파일을 삭제하고 새 파일로 교체",
            example = "null",
            nullable = true
    )
    private final List<Long> fileIds;
}
