package com.moci_3d_backend.domain.user.dto.response;

import com.moci_3d_backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {

    private UserResponse user;      // 사용자 기본 정보

    public static UserLoginResponse from(User user) {
        return new UserLoginResponse(UserResponse.from(user));
    }
}
