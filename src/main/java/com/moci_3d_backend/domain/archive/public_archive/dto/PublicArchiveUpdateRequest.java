package com.moci_3d_backend.domain.archive.public_archive.dto;

import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PublicArchiveUpdateRequest {
    private final String title;
    private final String description;
    private final ArchiveCategory category;
    private final String subCategory;
}
