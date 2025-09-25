package com.moci_3d_backend.domain.archive.archive_request.repository;

import com.moci_3d_backend.domain.archive.archive_request.entity.ArchiveRequest;
import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRequestRepository extends JpaRepository<ArchiveRequest, Long> {

    // 상태별 조회
    Page<ArchiveRequest> findByStatus(RequestStatus status, Pageable pageable);

    // 요청자별 조회 (Entity 필드명 user 사용)
    Page<ArchiveRequest> findByUser(User user, Pageable pageable);

    // 요청자 ID별 조회 (Entity 필드명 user 사용)
    Page<ArchiveRequest> findByUserId(Long userId, Pageable pageable);

    // 검토자별 조회
    Page<ArchiveRequest> findByReviewedBy(User reviewedBy, Pageable pageable);

    // 제목으로 검색 (부분 매칭)
    Page<ArchiveRequest> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // 상태와 제목으로 복합 검색
    Page<ArchiveRequest> findByStatusAndTitleContainingIgnoreCase(
            RequestStatus status, String title, Pageable pageable);

    // 특정 사용자의 특정 상태 요청들 조회 (Entity 필드명 user 사용)
    Page<ArchiveRequest> findByUserAndStatus(User user, RequestStatus status, Pageable pageable);

    // 대기중인 요청 개수
    long countByStatus(RequestStatus status);

    // 특정 사용자의 요청 개수 (Entity 필드명 user 사용)
    long countByUser(User user);

    List<ArchiveRequest> user(User user);
}