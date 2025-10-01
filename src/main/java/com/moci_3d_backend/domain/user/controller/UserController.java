package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.dto.request.UserDigitalLevelRequest;
import com.moci_3d_backend.domain.user.dto.request.UserEmailUpdateRequest;
import com.moci_3d_backend.domain.user.dto.request.UserPasswordUpdateRequest;
import com.moci_3d_backend.domain.user.dto.request.UserPhoneCheckRequest;
import com.moci_3d_backend.domain.user.dto.request.UserWithdrawRequest;
import com.moci_3d_backend.domain.user.dto.response.UserDigitalLevelResponse;
import com.moci_3d_backend.domain.user.dto.response.UserPhoneCheckResponse;
import com.moci_3d_backend.domain.user.dto.response.UserResponse;
import com.moci_3d_backend.domain.user.dto.response.UserWithdrawResponse;
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
    @Operation(
        summary = "내 정보 조회", 
        description = "현재 로그인한 사용자의 정보를 조회합니다. (인증 필요)"
    )
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

    // === 디지털 레벨 설정 ===
    @Operation(
        summary = "디지털 레벨 설정", 
        description = "로그인 후 디지털 레벨 설문조사 응답을 제출하여 디지털 레벨을 설정합니다. (인증 필요)"
    )
    @ApiResponse(responseCode = "200", description = "디지털 레벨 설정 성공")
    @PatchMapping("/digital-level")
    @Transactional
    public ResponseEntity<RsData<UserDigitalLevelResponse>> updateDigitalLevel(
            @Valid @RequestBody UserDigitalLevelRequest request) {
        
        User actor = rq.getActor();
        UserDigitalLevelResponse response = userService.updateDigitalLevel(actor, request);
        return ResponseEntity.ok(RsData.successOf(response));
    }
    
    // === 이메일 수정/등록 ===
    @Operation(
        summary = "이메일 수정/등록", 
        description = "사용자의 이메일을 등록하거나 수정합니다. (인증 필요)"
    )
    @ApiResponse(responseCode = "200", description = "이메일 수정 성공")
    @PatchMapping("/email")
    @Transactional
    public ResponseEntity<RsData<UserResponse>> updateEmail(
            @Valid @RequestBody UserEmailUpdateRequest request) {
        
        User actor = rq.getActor();
        User updatedUser = userService.updateEmail(actor, request);
        UserResponse response = UserResponse.from(updatedUser);
        return ResponseEntity.ok(RsData.successOf(response));
    }
    
    // === 비밀번호 변경 ===
    @Operation(
        summary = "비밀번호 변경", 
        description = "사용자의 비밀번호를 변경합니다. (인증 필요, 일반 로그인만 가능)"
    )
    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공")
    @ApiResponse(responseCode = "400", description = "소셜 로그인 사용자 또는 비밀번호 불일치")
    @PatchMapping("/password")
    @Transactional
    public ResponseEntity<RsData<Void>> updatePassword(
            @Valid @RequestBody UserPasswordUpdateRequest request) {
        
        User actor = rq.getActor();
        userService.updatePassword(actor, request);
        
        return ResponseEntity.ok(RsData.of(200, "비밀번호가 성공적으로 변경되었습니다."));
    }
    
    // === 회원 탈퇴 ===
    @Operation(
        summary = "회원 탈퇴", 
        description = "사용자 계정을 영구적으로 삭제합니다. (인증 필요, 복구 불가)"
    )
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
    @DeleteMapping("/me")
    @Transactional
    public ResponseEntity<RsData<UserWithdrawResponse>> withdrawUser(
            @Valid @RequestBody UserWithdrawRequest request) {
        
        User actor = rq.getActor();
        
        // 회원 탈퇴 처리 (탈퇴 정보 반환)
        UserWithdrawResponse response = userService.withdrawUser(actor, request);
        
        // JWT 토큰 쿠키 삭제 (로그아웃 처리)
        rq.deleteCookie("accessToken");
        rq.deleteCookie("refreshToken");
        
        return ResponseEntity.ok(RsData.of(200, response.getName() + "님의 회원 탈퇴가 완료되었습니다.", response));
    }
}
