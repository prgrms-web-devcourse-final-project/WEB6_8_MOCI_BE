package com.moci_3d_backend.domain.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    
    @NotNull(message = "로그인 타입은 필수입니다")
    @Pattern(regexp = "^(PHONE|KAKAO|NAVER)$", message = "지원하지 않는 로그인 타입입니다")
    private String loginType;
    
    // === 일반 로그인 ===
    private Integer userId;    // 전화번호
    private String password;  // 비밀번호
    
    // === 소셜 로그인 ===
    private String socialId;  // 암호화된 소셜 고유 ID
}
