package com.moci_3d_backend.domain.archive.public_archive.repository;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicArchiveRepository extends JpaRepository<PublicArchive, Long> {

    /**
     * title 또는 description에서 키워드를 검색
     * LIKE 검색으로 부분 일치를 지원
     */
    @Query("SELECT p FROM PublicArchive p " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PublicArchive> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
