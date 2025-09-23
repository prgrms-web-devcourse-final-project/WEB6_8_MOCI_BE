package com.moci_3d_backend.domain.user.dto.response;

import com.moci_3d_backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    
    private Long id;              // 시스템 식별용 (내부 사용)
    private String name;          // 유저이름 표시용

    public static UserDetailResponse from(User user) {
        return new UserDetailResponse(
            user.getId(),
            user.getName()
        );
    }
}
