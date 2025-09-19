package com.moci_3d_backend.domain.archive.public_archive.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicArchiveCreateRequest {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    private String description;

    private String category;

    private String subCategory;

    private Long fileId;
}
