package com.moci_3d_backend.domain.archive.public_archive.mapper;

import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveListResponse;
import com.moci_3d_backend.domain.archive.public_archive.dto.PublicArchiveResponse;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.fileUpload.dto.FileUploadDto;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.user.entity.User;
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

        // TODO: UserDto 구현되면 개선 (User 정보 추출)
        PublicArchiveResponse.UserDto userDto = createSafeUserDto(archive.getUploadedBy());

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

        // TODO: UserDto 구현되면 개선 (User 정보 추출)
        PublicArchiveResponse.UserDto userDto = createSafeUserDto(archive.getUploadedBy());

        return PublicArchiveListResponse.PublicArchiveDto.builder()
                .id(archive.getId())
                .title(archive.getTitle())
                .category(archive.getCategory())
                .subCategory(archive.getSubCategory())
                .uploadedBy(createSafeListUserDto(archive.getUploadedBy()))
                .createdAt(archive.getUploadedAt())
                .build();
    }

    // TODO: UserDto 구현되면 개선 (User 정보가 null일 때 대비)
    // 안전한 UserDto 생성 메서드
    private PublicArchiveResponse.UserDto createSafeUserDto(User user) {
        if (user == null) {
            return null;
        }

        return PublicArchiveResponse.UserDto.builder()
                .id(user.getId())
                .build();
    }

    // 안전한 ListUserDto 생성 메서드
    private PublicArchiveListResponse.PublicArchiveDto.UserDto createSafeListUserDto(User user) {
        if (user == null) {
            return null;
        }

        return PublicArchiveListResponse.PublicArchiveDto.UserDto.builder()
                .id(user.getId())
                .build();
    }
}
