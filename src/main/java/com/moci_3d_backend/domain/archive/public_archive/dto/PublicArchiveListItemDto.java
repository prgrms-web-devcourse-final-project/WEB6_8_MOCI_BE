package com.moci_3d_backend.domain.archive.public_archive.dto;

import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PublicArchiveListItemDto {
    private final Long id;
    private final String title;
    private final FileUploadDto thumbnail; // 첫 번째 이미지 (없으면 null)
    private final LocalDateTime createdAt;
}
