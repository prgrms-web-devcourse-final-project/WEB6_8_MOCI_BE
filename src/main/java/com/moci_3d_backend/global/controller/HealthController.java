package com.moci_3d_backend.global.controller;

import com.moci_3d_backend.global.rsData.RsData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public RsData<Void> health() {
        return RsData.of(200, "서버가 정상 작동 중입니다.");
    }
}