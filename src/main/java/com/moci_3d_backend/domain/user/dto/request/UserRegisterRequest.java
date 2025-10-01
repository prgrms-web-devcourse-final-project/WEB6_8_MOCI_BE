package com.moci_3d_backend.domain.user.dto.request;

import com.moci_3d_backend.domain.user.entity.User;
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
    // 필수 정보
    // ========================================
    @NotBlank(message = "실명은 필수입니다")
    private String name;
    
    // ========================================
    // 선택 정보 (마이페이지에서 등록/수정 가능)
    // ========================================
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;  // null 가능
    
    // ========================================
    // 일반 회원가입 필수 (loginType="PHONE"일 때)
    // ========================================
    @NotBlank(message = "전화번호는 필수입니다")
    @Pattern(regexp = "^01[016789]\\d{7,8}$", message = "올바른 전화번호 형식이 아닙니다 (하이픈 제외)")
    private String userId; // 전화번호 (하이픈 제거된 형식)
    
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password; // 비밀번호
    
    // === DTO → Entity 변환 메서드 ===
    public User toEntity() {
        User user = new User();
        
        // 1. 기본 정보 설정
        user.setUserId(this.userId);
        user.setLoginType(this.loginType);
        user.setPassword(this.password);  // Service에서 암호화함 (중복 방지)
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setName(this.name);
        user.setEmail(this.email);  // null 가능 (선택사항)
        
        // 2. 기본값 설정 
        user.setRole(User.UserRole.USER);  // 기본 역할: USER
        user.setDigitalLevel(null);  // 디지털 레벨: 미설정 (별도 API로 설정)
        user.setCreatedAt(LocalDateTime.now());  // 생성일시
        user.setUpdatedAt(LocalDateTime.now());  // 수정일시
        
        return user;
    }
}
