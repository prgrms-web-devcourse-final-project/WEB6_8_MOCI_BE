package com.moci_3d_backend.domain.archive.public_archive.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PublicArchiveUpdateRequest {

    @NotNull(message = "공개 자료실 ID는 필수입력 항목입닙다.")
    private Long id;

    private String title;

    private String description;

    private String category;

    private String subCategory;
}
