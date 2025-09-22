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
    
    // === 마이페이지 기능 관련 정보 ===
    // 1. 유저이름 표시
    // 2. 비밀번호 바꾸기 (별도 API)
    // 3. 회원탈퇴하기 (별도 API)
    
    // === 정적 팩토리 메서드 ===
    public static UserDetailResponse from(User user) {
        return new UserDetailResponse(
            user.getId(),
            user.getName()
        );
    }
}
