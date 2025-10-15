package com.moci_3d_backend.domain.fileUpload.entity;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String file_name;
    private String file_url;
    private String file_type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "public_archive_id")
    private PublicArchive publicArchive;

    public FileUpload(String file_name, String file_url, String file_type) {
        this.file_name = file_name;
        this.file_url = file_url;
        this.file_type = file_type;
    }
}
