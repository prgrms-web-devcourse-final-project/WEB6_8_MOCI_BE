package com.moci_3d_backend.domain.user.dto.response;

import com.moci_3d_backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithdrawResponse {
    
    private String userId;      // 탈퇴한 사용자 ID 
    private String name;        // 탈퇴한 사용자 이름
    private LocalDateTime withdrawnAt;  // 탈퇴 일시
    
    public static UserWithdrawResponse of(User user) {
        return UserWithdrawResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .withdrawnAt(LocalDateTime.now())
                .build();
    }
}

