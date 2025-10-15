package com.moci_3d_backend.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPhoneCheckResponse {
    
    private boolean isAvailable;  // 사용 가능 여부
    private String message;      // 결과 메시지
    
    public static UserPhoneCheckResponse available() {
        return UserPhoneCheckResponse.builder()
                .isAvailable(true)
                .message("사용 가능한 전화번호입니다.")
                .build();
    }
    
    public static UserPhoneCheckResponse unavailable() {
        return UserPhoneCheckResponse.builder()
                .isAvailable(false)
                .message("이미 사용 중인 전화번호입니다.")
                .build();
    }
}
