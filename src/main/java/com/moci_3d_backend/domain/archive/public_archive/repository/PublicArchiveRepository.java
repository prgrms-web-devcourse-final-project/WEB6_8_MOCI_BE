package com.moci_3d_backend.domain.archive.public_archive.repository;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PublicArchiveRepository extends JpaRepository<PublicArchive, Long> {
    // 공개 자료실 목록 페이징
    Page<PublicArchive> findAll(Pageable pageable);
}
