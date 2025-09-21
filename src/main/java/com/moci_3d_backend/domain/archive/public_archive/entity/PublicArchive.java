package com.moci_3d_backend.domain.archive.public_archive.entity;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "public_archive")
@Getter
@Setter
@NoArgsConstructor
public class PublicArchive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 255)
    private String category;

    @Column(name = "sub_category", length = 255)
    private String subCategory;

    // TODO: mappedBy는 files내 정의된 필드명으로 확인 및 수정할 것
//    @OneToMany(mappedBy = "publicArchive",  cascade = CascadeType.ALL, orphanRemoval = true) // 글 삭제시 파일도 같이 삭제(고아 삭제)
//    @JoinColumn(name = "file_id", referencedColumnName = "id", nullable = true) // file_id가 없어도 글 생성 가능
    private List<FileUpload> fileUploads = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", referencedColumnName = "id", nullable = false)
    private User uploadedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
