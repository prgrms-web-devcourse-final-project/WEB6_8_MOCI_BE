package com.moci_3d_backend.domain.archive.archive_request.dto;

import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArchiveRequestResponseDto {
    private final Long id;
    private final UserDto requester;
    private final String title;
    private final String description;
    private final RequestStatus status;
    private final UserDto reviewedBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Getter
    @Builder
    public static class UserDto {
        private final Long id;
        private final String name;
        private final String profileImageUrl; // 명세서에 있는 필드
    }
}