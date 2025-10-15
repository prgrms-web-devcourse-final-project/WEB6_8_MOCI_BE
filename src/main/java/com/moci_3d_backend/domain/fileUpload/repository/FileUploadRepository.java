package com.moci_3d_backend.domain.fileUpload.repository;

import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
}
