package com.moci_3d_backend.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckPhoneNumberResponse {
    
    private String phoneNumber;
    private boolean available; // true: 사용가능, false: 이미 사용중
    private String message;    // "사용 가능한 전화번호입니다" 또는 "이미 사용 중인 전화번호입니다"
    
    public static UserCheckPhoneNumberResponse from(String phoneNumber, boolean available) {
        String message = available ? "사용 가능한 전화번호입니다" : "이미 사용 중인 전화번호입니다";
        return new UserCheckPhoneNumberResponse(phoneNumber, available, message);
    }
}