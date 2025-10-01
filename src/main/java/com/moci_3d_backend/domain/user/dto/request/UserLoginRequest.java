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
    @NotBlank(message = "아이디(전화번호)는 필수입니다")
    private String userId;    // 전화번호
    
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;  // 비밀번호
}
