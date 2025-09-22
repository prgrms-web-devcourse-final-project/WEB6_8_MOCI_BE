package com.moci_3d_backend.domain.archive.public_archive.dto;

import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PublicArchiveResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String category;
    private final String subCategory;
    private final FileUploadDto file; // FileUploadDto 사용
    private final UserDto uploadedBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class UserDto {
        private final Long id;
        private final String name;
    }
}
