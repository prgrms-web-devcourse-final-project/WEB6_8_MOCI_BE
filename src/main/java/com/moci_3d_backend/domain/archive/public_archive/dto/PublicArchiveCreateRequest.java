package com.moci_3d_backend.domain.archive.public_archive.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PublicArchiveCreateRequest {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private final String title;

    private final String description;

    private final String category;

    private final String subCategory;

    private final Long fileId;
}
