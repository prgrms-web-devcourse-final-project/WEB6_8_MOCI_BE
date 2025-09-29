package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.dto.request.UserCheckPhoneNumberRequest;
import com.moci_3d_backend.domain.user.dto.response.UserCheckPhoneNumberResponse;
import com.moci_3d_backend.domain.user.dto.response.UserResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 관리", description = "사용자 정보 조회 및 관리 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final Rq rq;
    private final UserService userService;

    // === 사용자 조회 ===
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공")
    @GetMapping("/me")
    @Transactional(readOnly = true)
    public ResponseEntity<RsData<UserResponse>> getMe() {
        User actor = rq.getActor();

        UserResponse response = UserResponse.from(actor);
        return ResponseEntity.ok(RsData.successOf(response));
    }

    // === 전화번호 중복확인 ===
    @Operation(summary = "전화번호 중복확인", description = "회원가입시 전화번호 중복 확인")
    @ApiResponse(responseCode = "200", description = "전화번호 중복확인 완료")
    @PostMapping("/check-phone")
    @Transactional(readOnly = true)
    public ResponseEntity<RsData<UserCheckPhoneNumberResponse>> checkPhone(@RequestBody UserCheckPhoneNumberRequest request) {
        UserCheckPhoneNumberResponse response = userService.checkPhone(request);
        return ResponseEntity.ok(RsData.successOf(response));
    }

    // === 로그인 상태 응답 DTO ===
    public record LoginStatusResponse(
            boolean isLoggedIn,
            String userName,
            String nextPage,
            Boolean needsTest
    ) {}
}
