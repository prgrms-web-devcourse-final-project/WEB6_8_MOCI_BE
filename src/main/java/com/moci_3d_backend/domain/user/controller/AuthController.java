package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.dto.request.UserLoginRequest;
import com.moci_3d_backend.domain.user.dto.request.UserRegisterRequest;
import com.moci_3d_backend.domain.user.dto.response.UserCreateTokenResponse;
import com.moci_3d_backend.domain.user.dto.response.UserRegisterResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 관리", description = "사용자 인증 관련 API (회원가입, 로그인, 토큰 관리)")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Rq rq;
    private final UserService userService;

    @Value("${custom.frontend.baseUrl}")
    private String frontendBaseUrl;

    @Value("${custom.frontend.registerPath}")
    private String registerPath;

    @Value("${custom.frontend.mainPath}")
    private String mainPath;

    // === 회원가입 ===
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록하고 자동으로 로그인합니다.")
    @PostMapping("/register")
    public ResponseEntity<RsData<UserRegisterResponse>> signup(@RequestBody UserRegisterRequest request) {
        User user = userService.register(request);

        // ⭐ 회원가입 후 자동 로그인: JWT 토큰 생성 및 쿠키 설정
        rq.setCookie("accessToken", userService.genAccessToken(user));
        rq.setCookie("refreshToken", user.getRefreshToken());

        // 디지털 레벨에 따라 리다이렉트 URL 결정
        String redirectUrl = determineRedirectUrl(user);

        UserRegisterResponse response = UserRegisterResponse.of(user, redirectUrl);
        return ResponseEntity.ok(RsData.successOf(response));
    }

    // === 토큰 생성 ===
    @Operation(summary = "로그인(토큰발급)", description = "사용자 인증 후 JWT 토큰을 생성하고 쿠키에 저장합니다.")
    @PostMapping("/login")
    public ResponseEntity<RsData<UserCreateTokenResponse>> token(@RequestBody UserLoginRequest request) {
        User user = userService.auth(request);

        String refreshToken = user.getRefreshToken();

        rq.setCookie("accessToken", userService.genAccessToken(user));
        rq.setCookie("refreshToken", refreshToken);

        // 디지털 레벨에 따라 리다이렉트 URL 결정
        String redirectUrl = determineRedirectUrl(user);

        UserCreateTokenResponse tokenResponse = UserCreateTokenResponse.of(user, redirectUrl);

        return ResponseEntity.ok(RsData.successOf(tokenResponse));
    }

    @Operation(summary = "로그아웃", description = "저장된 JWT 토큰을 삭제합니다 (로그아웃).")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> deleteToken() {
        rq.deleteCookie("accessToken");
        rq.deleteCookie("refreshToken");

        return ResponseEntity.ok().build();
    }

    // === 리다이렉트 URL 결정 (디지털 레벨 기반) ===
    private String determineRedirectUrl(User user) {
        if (user.getDigitalLevel() == null) {
            // 디지털 레벨 미설정 → 디지털 레벨 설문조사 페이지로
            return frontendBaseUrl + registerPath;
        } else {
            // 디지털 레벨 설정됨 → 메인 페이지로
            return frontendBaseUrl + mainPath;
        }
    }
}
