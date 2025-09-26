package com.moci_3d_backend.domain.archive.public_archive.repository;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.archive.public_archive.entity.QPublicArchive;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PublicArchiveRepositoryImpl implements PublicArchiveRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PublicArchive> searchByKeywords(String[] keywords, Pageable pageable) {
        QPublicArchive archive = QPublicArchive.publicArchive;
        
        BooleanBuilder builder = new BooleanBuilder();
        
        // 각 키워드에 대해 title 또는 description에 포함되는 조건 추가
        // AND 조건: 모든 키워드가 포함되어야 함
        for (String keyword : keywords) {
            String lowerKeyword = keyword.toLowerCase();
            builder.and(
                archive.title.lower().contains(lowerKeyword)
                .or(archive.description.lower().contains(lowerKeyword))
            );
        }
        
        // 쿼리 실행
        JPAQuery<PublicArchive> query = queryFactory
                .selectFrom(archive)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        
        // 정렬 적용
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                if (order.getProperty().equals("createdAt")) {
                    query.orderBy(order.isAscending() ? 
                        archive.createdAt.asc() : archive.createdAt.desc());
                }
            });
        } else {
            // 정렬 없으면 기본 최신순
            query.orderBy(archive.createdAt.desc());
        }
        
        List<PublicArchive> results = query.fetch();
        
        // 전체 개수 조회
        Long total = queryFactory
                .select(archive.count())
                .from(archive)
                .where(builder)
                .fetchOne();
        
        return new PageImpl<>(results, pageable, total);
    }
}
