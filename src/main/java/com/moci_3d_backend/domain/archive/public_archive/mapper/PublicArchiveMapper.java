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

    // PublicArchive 엔티티를 PublicArchiveResponse DTO로 변환
    public PublicArchiveResponse toResponseDto(PublicArchive archive) {
        if (archive == null) return null;

        FileUploadDto fileDto = null;
        if (archive.getFileUploads() != null && !archive.getFileUploads().isEmpty()) {
            FileUpload firstFile = archive.getFileUploads().get(0);
            fileDto = FileUploadDto.from(firstFile);
        }

        PublicArchiveResponse.UserDto userDto = mapToUserDto(archive.getUploadedBy());

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

    // PublicArchive 엔티티를 PublicArchiveListResponse의 PublicArchiveDto로 변환
    public PublicArchiveListResponse.PublicArchiveDto toListDto(PublicArchive archive) {
        if (archive == null) return null;

        return PublicArchiveListResponse.PublicArchiveDto.builder()
                .id(archive.getId())
                .title(archive.getTitle())
                .category(archive.getCategory())
                .subCategory(archive.getSubCategory())
                .uploadedBy(mapToListUserDto(archive.getUploadedBy()))
                .createdAt(archive.getUploadedAt())
                .build();
    }

    // FileUpload 리스트에서 첫 번째 파일을 FileUploadDto로 변환
    private FileUploadDto mapToFileDto(java.util.List<FileUpload> fileUploads) {
        if (fileUploads == null || fileUploads.isEmpty()) {
            return null;
        }
        FileUpload firstFile = fileUploads.get(0); // Assuming only one file is associated
        return FileUploadDto.from(firstFile);
    }

    // User 엔티티를 PublicArchiveResponse의 UserDto로 변환
    private PublicArchiveResponse.UserDto mapToUserDto(User user) {
        if (user == null) {
            return PublicArchiveResponse.UserDto.builder()
                    .id(null)
                    .name("알수없음")
                    .build();
        }

        return PublicArchiveResponse.UserDto.builder()
                .id(user.getId())
                .name(user.getName() != null ? user.getName() : "이름 없음") // name이 null인 경우 기본값
                .build();
    }

    // User 엔티티를 PublicArchiveListResponse.PublicArchiveDto.UserDto로 변환
    private PublicArchiveListResponse.PublicArchiveDto.UserDto mapToListUserDto(User user) {
        if (user == null) {
            return PublicArchiveListResponse.PublicArchiveDto.UserDto.builder()
                    .id(null)
                    .name("알 수 없음")
                    .build();
        }

        return PublicArchiveListResponse.PublicArchiveDto.UserDto.builder()
                .id(user.getId())
                .name(user.getName() != null ? user.getName() : "이름 없음") // name이 null인 경우 기본값
                .build();
    }
}
