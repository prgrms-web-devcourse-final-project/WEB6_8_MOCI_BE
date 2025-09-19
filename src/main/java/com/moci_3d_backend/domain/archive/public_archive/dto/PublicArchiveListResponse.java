package com.moci_3d_backend.domain.archive.public_archive.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class PublicArchiveListResponse {

    private int totalPages;
    private long totalElements;
    private int currentPage;
    private List<PublicArchiveDto> archives;

    // 내부 클래스 사용
    @Getter
    @Builder
    public static class PublicArchiveDto {
        private Long id;
        private String title;
        private String category;
        private String subCategory;
        private UserDto uploadedBy;
        private LocalDateTime createdAt;

        @Getter
        @Builder
        public static class UserDto {
            private Long id;
            private String name;
        }
    }
}
