package com.moci_3d_backend.domain.user.dto.response;

import com.moci_3d_backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {

    private UserResponse user;      // 사용자 기본 정보

    // === 정적 팩토리 메서드 ===
    public static UserLoginResponse from(User user) {
        return new UserLoginResponse(UserResponse.from(user));
    }
}
