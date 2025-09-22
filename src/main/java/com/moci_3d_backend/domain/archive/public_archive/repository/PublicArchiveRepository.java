package com.moci_3d_backend.domain.archive.public_archive.repository;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicArchiveRepository extends JpaRepository<PublicArchive, Long> {
}
