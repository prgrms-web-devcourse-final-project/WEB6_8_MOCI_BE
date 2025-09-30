package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.dto.request.UserPhoneCheckRequest;
import com.moci_3d_backend.domain.user.dto.response.UserResponse;
import com.moci_3d_backend.domain.user.dto.response.UserPhoneCheckResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "사용자 관리", description = "사용자 정보 조회 및 검증 관련 API")
public class UserController {
    private final Rq rq;
    private final UserService userService;

    // === 사용자 조회 ===
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다. (인증 필요)")
    @GetMapping("/me")
    @Transactional(readOnly = true)
    public ResponseEntity<RsData<UserResponse>> getMe() {
        User actor = rq.getActor();

        UserResponse response = UserResponse.from(actor);
        return ResponseEntity.ok(RsData.successOf(response));
    }

    // === 전화번호 중복확인 ===
    @Operation(
        summary = "전화번호 중복확인", 
        description = "회원가입 시 전화번호 중복 여부를 확인합니다. (인증 불필요)"
    )
    @PostMapping("/phone-check")
    @Transactional(readOnly = true)
    public ResponseEntity<RsData<UserPhoneCheckResponse>> checkPhoneDuplicate(
            @Valid @RequestBody UserPhoneCheckRequest request) {
        
        UserPhoneCheckResponse response = userService.checkPhoneDuplicate(request);
        return ResponseEntity.ok(RsData.successOf(response));
    }
}
