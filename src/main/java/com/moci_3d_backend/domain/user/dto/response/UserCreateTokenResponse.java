package com.moci_3d_backend.domain.user.dto.response;

import com.moci_3d_backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateTokenResponse {

    private UserResponse user;      // 사용자 기본 정보
    private String redirectUrl;     // 리다이렉트할 URL (디지털 레벨에 따라)

    public static UserCreateTokenResponse from(User user) {
        return new UserCreateTokenResponse(
                UserResponse.from(user),
                null  // Controller에서 설정
        );
    }
    
    public static UserCreateTokenResponse of(User user, String redirectUrl) {
        return new UserCreateTokenResponse(
                UserResponse.from(user),
                redirectUrl
        );
    }
}
