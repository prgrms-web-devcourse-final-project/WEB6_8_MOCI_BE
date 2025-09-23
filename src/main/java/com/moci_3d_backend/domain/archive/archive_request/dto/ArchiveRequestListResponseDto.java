package com.moci_3d_backend.domain.archive.archive_request.dto;

import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ArchiveRequestListResponseDto {
    private final int totalPages;
    private final long totalElements;
    private final int currentPage;
    private final List<RequestSummaryDto> requests;

    @Getter
    @Builder
    public static class RequestSummaryDto {
        private final Long id;
        private final String title;
        private final String requesterName;
        private final RequestStatus status;
        private final LocalDateTime createdAt;
    }
}