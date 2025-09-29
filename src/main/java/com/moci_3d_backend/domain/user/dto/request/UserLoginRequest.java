package com.moci_3d_backend.domain.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    
    @NotNull(message = "로그인 타입은 필수입니다")
    @Pattern(regexp = "^(PHONE|KAKAO|NAVER)$", message = "지원하지 않는 로그인 타입입니다")
    private String loginType;
    
    // === 일반 로그인 ===
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다")
    private String userId;    // 전화번호 (010-1234-5678 형식)
    private String password;  // 비밀번호
}
