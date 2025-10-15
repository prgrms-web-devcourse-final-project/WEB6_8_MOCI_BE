package com.moci_3d_backend.domain.archive.public_archive.entity;

public enum ArchiveCategory {
    KAKAO_TALK,
    YOUTUBE,
    KTX,
    INTERCITY_BUS,
    BAEMIN,
    COUPANG;

    public static ArchiveCategory fromString(String category) {
        if (category == null || category.trim().isEmpty()) {
            return null;
        }
        try {
            return ArchiveCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
}
