package com.moci_3d_backend.domain.user.dto.request;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.util.PasswordUtil;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    
    // ========================================
    // 공통 필수
    // ========================================
    @NotNull(message = "로그인 타입은 필수입니다")
    @Pattern(regexp = "^(PHONE|KAKAO|NAVER)$", message = "지원하지 않는 로그인 타입입니다")
    private String loginType;
    
    // ========================================
    // 조건부
    // ========================================
    @NotBlank(message = "실명은 필수입니다")
    private String name;
    
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    // ========================================
    // 일반 회원가입
    // ========================================
    private String userId; // 전화번호
    private String password; // 비밀번호
    
    // === DTO → Entity 변환 메서드 ===
    public User toEntity() {
        User user = new User();
        
        // 1. 기본 정보 설정
        user.setUserId(this.userId);
        user.setLoginType(this.loginType);
        user.setPassword(PasswordUtil.encode(this.password));
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setName(this.name);
        user.setEmail(this.email);
        
        // 2. 기본값 설정 
        user.setRole(User.UserRole.USER);  // 기본 역할: USER
        user.setDigitalLevel(null);  // 디지털 레벨: 미설정 (별도 API로 설정)
        user.setCreatedAt(LocalDateTime.now());  // 생성일시
        user.setUpdatedAt(LocalDateTime.now());  // 수정일시
        
        return user;
    }
}
