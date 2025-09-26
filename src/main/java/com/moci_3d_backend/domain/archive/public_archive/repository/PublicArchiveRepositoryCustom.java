package com.moci_3d_backend.domain.archive.public_archive.repository;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// QueryDSL을 사용한 복잡한 검색 쿼리 처리
public interface PublicArchiveRepositoryCustom {
    
    /**
     * 여러 키워드로 검색
     * 모든 키워드가 title 또는 description에 포함된 글을 검색
     * 
     * @param keywords 검색할 키워드 배열
     * @param pageable 페이징 정보
     * @return 검색 결과 (페이징)
     */
    Page<PublicArchive> searchByKeywords(String[] keywords, Pageable pageable);
}
