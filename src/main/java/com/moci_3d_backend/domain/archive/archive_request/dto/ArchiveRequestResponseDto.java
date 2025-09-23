package com.moci_3d_backend.domain.archive.archive_request.dto;

import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import com.moci_3d_backend.domain.user.dto.response.UserDetailResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArchiveRequestResponseDto {
    private final Long id;
    private final UserDetailResponse requester;
    private final String title;
    private final String description;
    private final RequestStatus status;
    private final UserDetailResponse reviewedBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}