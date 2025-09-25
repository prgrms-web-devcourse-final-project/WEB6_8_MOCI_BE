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
public class UserRegisterResponse {
    private Long id;
    private String name;
    private String email;
    private Integer digitalLevel;
    
    public static UserRegisterResponse from(User user) {
        return new UserRegisterResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getDigitalLevel()
        );
    }
}