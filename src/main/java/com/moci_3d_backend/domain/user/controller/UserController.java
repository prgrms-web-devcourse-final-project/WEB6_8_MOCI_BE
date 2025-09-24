package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.dto.response.UserResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    // === 사용자 조회 ===
    @GetMapping("/{userId}")
    public ResponseEntity<RsData<UserResponse>> getUserInfo(@PathVariable Integer userId) {
        User user = userService.findByUserId(userId);
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.ok(RsData.successOf(response));
    }
}
