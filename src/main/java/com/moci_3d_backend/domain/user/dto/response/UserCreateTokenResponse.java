package com.moci_3d_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateTokenResponse {

    private UserResponse user;      // 사용자 기본 정보

    public static UserCreateTokenResponse from(UserResponse user) {
        return new UserCreateTokenResponse(user);
    }
}
