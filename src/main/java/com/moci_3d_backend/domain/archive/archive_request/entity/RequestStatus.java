package com.moci_3d_backend.domain.archive.archive_request.entity;

public enum RequestStatus {
    PENDING("대기중"),
    APPROVED("승인됨"),
    REJECTED("거부됨");

    private final String description;

    RequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 편의 메서드들
    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isApproved() {
        return this == APPROVED;
    }

    public boolean isRejected() {
        return this == REJECTED;
    }

    // 문자열로부터 RequestStatus 찾기
    public static RequestStatus fromString(String status) {
        for (RequestStatus requestStatus : RequestStatus.values()) {
            if (requestStatus.name().equalsIgnoreCase(status)) {
                return requestStatus;
            }
        }
        // 유효하지 않은 값에 대한 예외 처리
        String validValues = String.join(", ",
                java.util.Arrays.stream(RequestStatus.values())
                        .map(RequestStatus::name)
                        .toArray(String[]::new));
        throw new IllegalArgumentException(
                String.format("Unknown RequestStatus: \"%s\". Valid values are: %s", status, validValues)
        );
    }
}
