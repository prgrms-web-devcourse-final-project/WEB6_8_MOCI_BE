package com.moci_3d_backend.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDigitalLevelResponse {
    
    private Integer digitalLevel;  // 설정된 디지털 레벨 (0~5)
    private String message;        // 결과 메시지
    
    public static UserDigitalLevelResponse of(Integer digitalLevel) {
        return UserDigitalLevelResponse.builder()
                .digitalLevel(digitalLevel)
                .message(String.format("디지털 레벨이 %d로 설정되었습니다.", digitalLevel))
                .build();
    }
}
