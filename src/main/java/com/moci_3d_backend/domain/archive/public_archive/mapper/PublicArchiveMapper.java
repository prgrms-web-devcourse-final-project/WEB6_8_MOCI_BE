package com.moci_3d_backend.domain.archive.public_archive.mapper;

import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveListResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveResponse;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import org.springframework.stereotype.Component;

@Component
public class PublicArchiveMapper {

    public PublicArchiveResponse toResponseDto(PublicArchive archive) {
        if (archive == null) return null;

        FileUploadDto fileDto = null;
        if (archive.getFileUploads() != null && !archive.getFileUploads().isEmpty()) {
            FileUpload firstFile = archive.getFileUploads().get(0);
            fileDto = FileUploadDto.from(firstFile);
        }

        // TODO: User관련 DTO 생성되면 개선
        PublicArchiveResponse.UserDto userDto = PublicArchiveResponse.UserDto.builder()
                .id(archive.getUploadedBy().getId())
                .name(archive.getUploadedBy().getName())
                .build();

        return PublicArchiveResponse.builder()
                .id(archive.getId())
                .title(archive.getTitle())
                .description(archive.getDescription())
                .category(archive.getCategory())
                .subCategory(archive.getSubCategory())
                .file(fileDto)
                .uploadedBy(userDto)
                .createdAt(archive.getUploadedAt())
                .updatedAt(archive.getUpdatedAt())
                .build();
    }

    public PublicArchiveListResponse.PublicArchiveDto toListDto(PublicArchive archive) {

        if (archive == null) return null;

        return PublicArchiveListResponse.PublicArchiveDto.builder()
                .id(archive.getId())
                .title(archive.getTitle())
                .category(archive.getCategory())
                .subCategory(archive.getSubCategory())
                .uploadedBy(PublicArchiveListResponse.PublicArchiveDto.UserDto.builder()
                        .id(archive.getUploadedBy().getId())
                        .name(archive.getUploadedBy().getName()) // User 엔티티에 getName()이 있다고 가정
                        .build())
                .createdAt(archive.getUploadedAt())
                .build();
    }
}
