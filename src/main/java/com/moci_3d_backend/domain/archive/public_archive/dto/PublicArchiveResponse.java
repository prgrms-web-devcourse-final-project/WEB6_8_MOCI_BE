package com.moci_3d_backend.domain.archive.public_archive.dto;

import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PublicArchiveResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String category;
    private final String subCategory;
    private final List<FileUploadDto> files; // 모든 파일들
    private final PublicArchiveUserDto uploadedBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
