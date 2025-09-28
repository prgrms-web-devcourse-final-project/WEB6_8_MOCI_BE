package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.dto.response.UserResponse;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final Rq rq;
    private final UserService userService;

    // === 사용자 조회 ===
    @GetMapping("/me")
    @Transactional(readOnly = true)
    public ResponseEntity<RsData<UserResponse>> getMe() {
        User actor = rq.getActor();

        UserResponse response = UserResponse.from(actor);
        return ResponseEntity.ok(RsData.successOf(response));
    }
}
