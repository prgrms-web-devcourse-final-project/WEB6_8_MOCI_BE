package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.dto.request.UserLoginRequest;
import com.moci_3d_backend.domain.user.dto.response.UserLoginResponse;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    // === 로그인 ===
    @PostMapping("/login")
    public ResponseEntity<RsData<UserLoginResponse>> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.ok(RsData.successOf(response));
    }
    
    @PostMapping("/social-login")
    public ResponseEntity<RsData<UserLoginResponse>> socialLogin(@RequestParam String socialId) {
        UserLoginResponse response = userService.socialLogin(socialId);
        return ResponseEntity.ok(RsData.successOf(response));
    }
}
