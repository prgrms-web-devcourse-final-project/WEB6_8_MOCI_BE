package com.moci_3d_backend.domain.archive.public_archive.repository;

import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicArchiveRepository extends JpaRepository<PublicArchive, Long>,
                                                            PublicArchiveRepositoryCustom {
    // 카테고리별 조회
    Page<PublicArchive> findByCategory(ArchiveCategory category, Pageable pageable);

    /**
     * title 또는 description에서 키워드를 검색
     * LIKE 검색으로 부분 일치를 지원
     */
    @Query("SELECT p FROM PublicArchive p " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PublicArchive> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 키워드 검색 + 카테고리 필터
    @Query("SELECT p FROM PublicArchive p " +
            "WHERE p.category = :category " +
            "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<PublicArchive> searchByKeywordAndCategory(
            @Param("keyword") String keyword,
            @Param("category") ArchiveCategory category,
            Pageable pageable
    );

    // 다중 키워드 검색 (전체 카테고리) - QueryDSL Custom 메서드
    Page<PublicArchive> searchByKeywords(String[] keywords, Pageable pageable);

    // 다중 키워드 검색 + 카테고리 필터 - QueryDSL Custom 메서드
    Page<PublicArchive> searchByKeywordsAndCategory(
            String[] keywords,
            ArchiveCategory category,
            Pageable pageable
    );

}
