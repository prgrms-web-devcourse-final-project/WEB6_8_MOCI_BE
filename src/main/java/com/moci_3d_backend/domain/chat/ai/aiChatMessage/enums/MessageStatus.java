package com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums;

import lombok.Getter;

@Getter
public enum MessageStatus {
    DELIVERED("전달완료"),
    READ("읽음"),
    FAILED("전달실패");

    private final String koreanName;  // 한글명을 저장할 필드

    MessageStatus(String koreanName) {
        this.koreanName = koreanName;
    }

}
