package com.moci_3d_backend.domain.archive.public_archive.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PublicArchiveListResponse {

    private final int totalPages;
    private final long totalElements;
    private final int currentPage;
    private final List<PublicArchiveDto> archives;

    // Page 객체를 받아 필드를 초기화 하는 생성자
    public PublicArchiveListResponse(Page<PublicArchiveDto> dtoPage) {
        this.totalPages = dtoPage.getTotalPages();
        this.totalElements = dtoPage.getTotalElements();
        this.currentPage = dtoPage.getNumber(); // page 번호는 0부터 시작
        this.archives = dtoPage.getContent();
    }

    // 내부 클래스 사용
    @Getter
    @Builder
    public static class PublicArchiveDto {
        private final Long id;
        private final String title;
        private final String category;
        private final String subCategory;
        private final UserDto uploadedBy;
        private final LocalDateTime createdAt;

        @Getter
        @Builder
        public static class UserDto {
            private final Long id;
            private final String name;
        }
    }
}
