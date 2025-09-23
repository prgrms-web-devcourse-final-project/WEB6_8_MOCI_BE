package com.moci_3d_backend.domain.archive.archive_request.mapper;

import com.moci_3d_backend.domain.archive.archive_request.dto.ArchiveRequestListResponseDto;
import com.moci_3d_backend.domain.archive.archive_request.dto.ArchiveRequestResponseDto;
import com.moci_3d_backend.domain.archive.archive_request.entity.ArchiveRequest;
import com.moci_3d_backend.domain.user.dto.response.UserDetailResponse;
import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ArchiveRequestMapper {

    // ArchiveRequest Entity를 상세 응답 DTO로 변환
    public ArchiveRequestResponseDto toResponseDto(ArchiveRequest archiveRequest) {
        if (archiveRequest == null) return null;

        return ArchiveRequestResponseDto.builder()
                .id(archiveRequest.getId())
                .requester(createUserDetailResponse(archiveRequest.getUser()))
                .title(archiveRequest.getTitle())
                .description(archiveRequest.getDescription())
                .status(archiveRequest.getStatus())
                .reviewedBy(createUserDetailResponse(archiveRequest.getReviewedBy()))
                .createdAt(archiveRequest.getCreatedAt())
                .updatedAt(archiveRequest.getUpdatedAt())
                .build();
    }

    // ArchiveRequest Entity를 목록용 요약 DTO로 변환
    public ArchiveRequestListResponseDto.RequestSummaryDto toSummaryDto(ArchiveRequest archiveRequest) {
        if (archiveRequest == null) return null;

        return ArchiveRequestListResponseDto.RequestSummaryDto.builder()
                .id(archiveRequest.getId())
                .title(archiveRequest.getTitle())
                .requesterName(getUserName(archiveRequest.getUser()))
                .status(archiveRequest.getStatus())
                .createdAt(archiveRequest.getCreatedAt())
                .build();
    }


    // User Entity를 UserDetailResponse로 변환 (null 체크 포함)
    private UserDetailResponse createUserDetailResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserDetailResponse.from(user);
    }

    //사용자 이름 추출
    private String getUserName(User user) {
        return user != null ? user.getName() : "알 수 없음";
    }
}