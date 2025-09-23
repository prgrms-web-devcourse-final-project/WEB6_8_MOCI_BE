package com.moci_3d_backend.domain.archive.archive_request.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ArchiveRequestUpdateDto extends ArchiveRequestBaseDto{
    public ArchiveRequestUpdateDto(String title, String description) {
        super(title, description);
    }
}
