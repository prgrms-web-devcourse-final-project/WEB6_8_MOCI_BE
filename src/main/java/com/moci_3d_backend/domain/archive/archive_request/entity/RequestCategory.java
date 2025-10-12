package com.moci_3d_backend.domain.archive.archive_request.entity;

public enum RequestCategory {
    KAKAO_TALK("카카오톡"),
    YOUTUBE("유튜브"),
    KTX("KTX"),
    INTERCITY_BUS("시외버스"),
    BAEMIN("배달의민족"),
    COUPANG("쿠팡"),
    ETC("기타");

    private final String description;

    RequestCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 문자열로부터 RequestCategory 찾기
    public static RequestCategory fromString(String category) {
        for (RequestCategory requestCategory : RequestCategory.values()) {
            if (requestCategory.name().equalsIgnoreCase(category)) {
                return requestCategory;
            }
        }
        throw new IllegalArgumentException("Invalid RequestCategory: " + category);
    }
}
