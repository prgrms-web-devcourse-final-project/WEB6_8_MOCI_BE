package com.moci_3d_backend.domain.archive.archive_request.entity;

import com.moci_3d_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "archive_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // 편의 메서드
    public void approve(User reviewer) {
        this.status = RequestStatus.APPROVED;
        this.reviewedBy = reviewer;
    }

    public void reject(User reviewer) {
        this.status = RequestStatus.REJECTED;
        this.reviewedBy = reviewer;
    }

    public boolean isPending() {
        return this.status == RequestStatus.PENDING;
    }

    public boolean isApproved() {
        return this.status == RequestStatus.APPROVED;
    }

    public boolean isRejected() {
        return this.status == RequestStatus.REJECTED;
    }
}
