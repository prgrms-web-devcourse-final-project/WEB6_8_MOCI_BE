package com.moci_3d_backend.domain.archive.public_archive.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PublicArchiveResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String subCategory;
    private FileDto file;
    private UserDto uploadedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 내부 클래스 사용
    @Getter
    @Builder
    public static class FileDto {
        private Long id;
        private String fileName;
        private String url;
    }

    @Getter
    @Builder
    public static class UserDto {
        private Long id;
        private String name;
    }
}
