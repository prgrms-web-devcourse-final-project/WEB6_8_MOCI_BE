package com.moci_3d_backend.domain.archive.public_archive.mapper;

import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveListItemDto;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveUserDto;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PublicArchiveMapper {

    // PublicArchive 엔티티를 PublicArchiveResponse DTO로 변환 (상세 조회용)
    public PublicArchiveResponse toResponseDto(PublicArchive archive) {
        if (archive == null) return null;

        // 모든 파일 처리
        List<FileUploadDto> fileDtos = Collections.emptyList();
        if (archive.getFileUploads() != null && !archive.getFileUploads().isEmpty()) {
            fileDtos = archive.getFileUploads().stream()
                    .map(FileUploadDto::from)
                    .toList();
        }

        PublicArchiveUserDto userDto = mapToUserDto(archive.getUploadedBy());

        return PublicArchiveResponse.builder()
                .id(archive.getId())
                .title(archive.getTitle())
                .description(archive.getDescription())
                .category(archive.getCategory() != null ? archive.getCategory().name() : null)
                .subCategory(archive.getSubCategory())
                .files(fileDtos)
                .uploadedBy(userDto)
                .createdAt(archive.getCreatedAt())
                .updatedAt(archive.getUpdatedAt())
                .build();
    }

    // PublicArchive 엔티티를 PublicArchiveListItemDto로 변환 (목록 조회용)
    public PublicArchiveListItemDto toListItemDto(PublicArchive archive) {
        if (archive == null) return null;

        // 첫 번째 이미지만 (썸네일)
        FileUploadDto thumbnail = null;
        if (archive.getFileUploads() != null && !archive.getFileUploads().isEmpty()) {
            thumbnail = FileUploadDto.from(archive.getFileUploads().get(0));
        }

        return PublicArchiveListItemDto.builder()
                .id(archive.getId())
                .title(archive.getTitle())
                .thumbnail(thumbnail)
                .createdAt(archive.getCreatedAt())
                .build();
    }
    
    // User 엔티티를 PublicArchiveUserDto로 변환
    private PublicArchiveUserDto mapToUserDto(User user) {
        if (user == null) {
            return PublicArchiveUserDto.builder()
                    .id(null)
                    .name("알 수 없음")
                    .build();
        }

        return PublicArchiveUserDto.builder()
                .id(user.getId())
                .name(user.getName() != null ? user.getName() : "이름 없음")
                .build();
    }
}
