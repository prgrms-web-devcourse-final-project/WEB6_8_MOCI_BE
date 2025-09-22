package com.moci_3d_backend.domain.user.dto.response;

import com.moci_3d_backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String name;
    private String email;
    private User.UserRole role;
    private Integer digitalLevel;
    private LocalDateTime createdAt;
    
    // === 정적 팩토리 메서드 ===
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getDigitalLevel(),
            user.getCreatedAt()
        );
    }
}
