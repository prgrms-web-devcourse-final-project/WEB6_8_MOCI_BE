package com.moci_3d_backend.domain.archive.public_archive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class PublicArchiveCreateRequest {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private final String title;

    private final String description;

    private final String category;

    private final String subCategory;

    @Schema(
            description = "업로드된 파일 ID 목록 (선택사항, 파일이 없으면 null)",
            example = "null",  // Swagger에서 null로 표시
            nullable = true
    )
    private final List<Long> fileIds;
}
