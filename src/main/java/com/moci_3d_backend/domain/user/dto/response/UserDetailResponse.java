package com.moci_3d_backend.domain.user.dto.response;

import com.moci_3d_backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    
    private Long id;
    private String userId;        // 마스킹 처리된 전화번호 (예: 010****5678)
    private String name;
    private String email;
    private String loginType;
    private User.UserRole role;
    private Integer digitalLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // === 정적 팩토리 메서드 ===
    public static UserDetailResponse from(User user) {
        return new UserDetailResponse(
            user.getId(),
            maskUserId(user.getUserId()),  // 전화번호 마스킹
            user.getName(),
            user.getEmail(),
            user.getLoginType(),
            user.getRole(),
            user.getDigitalLevel(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
    // === 유틸리티 메서드 ===
    private static String maskUserId(String userId) {
        if (userId == null || userId.length() != 11) {
            return null;
        }
        // 010****5678 형태로 마스킹
        return userId.substring(0, 3) + "****" + userId.substring(7);
    }
}
