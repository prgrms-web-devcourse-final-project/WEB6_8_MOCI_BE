package com.moci_3d_backend.domain.archive.archive_request.dto;

import com.moci_3d_backend.domain.archive.archive_request.entity.RequestCategory;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ArchiveRequestUpdateDto extends ArchiveRequestBaseDto{
    public ArchiveRequestUpdateDto(String title, String description, RequestCategory category) {
        super(title, description, category);
    }
}
