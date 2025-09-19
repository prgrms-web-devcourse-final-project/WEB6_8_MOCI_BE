package com.moci_3d_backend.domain.fileUpload.entity;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUpload {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String file_name;
    private String file_url;
    private String file_type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "public_archive_id")
    private PublicArchive publicArchive;
}
