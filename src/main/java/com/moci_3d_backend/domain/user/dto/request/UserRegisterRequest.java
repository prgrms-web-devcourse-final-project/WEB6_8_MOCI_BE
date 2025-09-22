package com.moci_3d_backend.domain.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    
    // ========================================
    // 공통 필수 필드 (모든 회원가입에서 필수)
    // ========================================
    @NotNull(message = "로그인 타입은 필수입니다")
    @Pattern(regexp = "^(PHONE|KAKAO|NAVER)$", message = "지원하지 않는 로그인 타입입니다")
    private String loginType;
    
    @NotNull(message = "디지털 레벨 설문조사는 필수입니다")
    @Size(min = 5, max = 5, message = "디지털 레벨 설문조사는 5문항입니다")
    private List<Boolean> digitalLevelAnswers;
    
    // ========================================
    // 조건부 필수 필드 (로그인 타입에 따라 달라짐)
    // ========================================
    @NotBlank(message = "실명은 필수입니다")
    private String name;
    
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    // ========================================
    // 일반 회원가입 전용 필드
    // ========================================
    private String userId; // 전화번호
    private String password; // 비밀번호
    
    // ========================================
    // 소셜 회원가입 전용 필드
    // ========================================
    private String socialId; // 암호화된 소셜 고유 ID
}
