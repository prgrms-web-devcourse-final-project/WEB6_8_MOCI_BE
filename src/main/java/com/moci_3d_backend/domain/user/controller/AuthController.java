package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.dto.request.UserLoginRequest;
import com.moci_3d_backend.domain.user.dto.request.UserRegisterRequest;
import com.moci_3d_backend.domain.user.dto.response.UserCreateTokenResponse;
import com.moci_3d_backend.domain.user.dto.response.UserLoginResponse;
import com.moci_3d_backend.domain.user.dto.response.UserRegisterResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Rq rq;
    private final UserService userService;

    // === 회원가입 ===
    @PostMapping("/register")
    public ResponseEntity<RsData<UserRegisterResponse>> signup(@RequestBody UserRegisterRequest request) {
        User user = userService.register(request);
        UserRegisterResponse response = UserRegisterResponse.from(user);
        return ResponseEntity.ok(RsData.successOf(response));
    }

    // === 로그인 ===
    @PostMapping("/login")
    public ResponseEntity<RsData<UserLoginResponse>> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.ok(RsData.successOf(response));
    }

    // === 로그인 ===
    @PostMapping("/token")
    public ResponseEntity<RsData<UserCreateTokenResponse>> token(@RequestBody UserLoginRequest request) {
        User user = userService.auth(request);

        String refreshToken = user.getRefreshToken();

        rq.setCookie("accessToken", userService.genAccessToken(user));
        rq.setCookie("refreshToken", refreshToken);

        UserCreateTokenResponse tokenResponse = UserCreateTokenResponse.from(user);

        return ResponseEntity.ok(RsData.successOf(tokenResponse));
    }
}
