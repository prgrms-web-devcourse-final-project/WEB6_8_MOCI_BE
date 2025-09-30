package com.moci_3d_backend.domain.archive.public_archive.repository;

import com.moci_3d_backend.domain.archive.public_archive.entity.ArchiveCategory;
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

        // 모든 키워드가 포함되어야 함 (AND 조건)
        BooleanBuilder builder = new BooleanBuilder();
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
                .where(builder);

        // 정렬 적용
        applySorting(query, archive, pageable);

        // 페이징 적용
        List<PublicArchive> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        long total = queryFactory
                .selectFrom(archive)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    // 다중 키워드 + 카테고리 검색
    @Override
    public Page<PublicArchive> searchByKeywordsAndCategory(
            String[] keywords,
            ArchiveCategory category,
            Pageable pageable
    ) {
        QPublicArchive archive = QPublicArchive.publicArchive;

        // 카테고리 필터 추가
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(archive.category.eq(category)); // 카테고리 조건

        // 모든 키워드가 포함되어야 함 (AND 조건)
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
                .where(builder);

        // 정렬 적용
        applySorting(query, archive, pageable);

        // 페이징 적용
        List<PublicArchive> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        long total = queryFactory
                .selectFrom(archive)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    // 정렬 적용 헬퍼 메서드
    private void applySorting(JPAQuery<PublicArchive> query, QPublicArchive archive, Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                String property = order.getProperty();
                if ("createdAt".equals(property)) {
                    query.orderBy(order.isAscending() ?
                            archive.createdAt.asc() : archive.createdAt.desc());
                } else if ("updatedAt".equals(property)) {
                    query.orderBy(order.isAscending() ?
                            archive.updatedAt.asc() : archive.updatedAt.desc());
                } else if ("title".equals(property)) {
                    query.orderBy(order.isAscending() ?
                            archive.title.asc() : archive.title.desc());
                }
            });
        } else {
            query.orderBy(archive.createdAt.desc());
        }
    }
}