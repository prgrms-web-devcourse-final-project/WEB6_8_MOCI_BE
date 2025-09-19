package com.moci_3d_backend.domain.test.controller;

import com.moci_3d_backend.domain.test.service.TestService;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Tag(name = "Test", description = "테스트 API")
public class TestController {

    private final TestService testService;

    @GetMapping("/ping")
    @Operation(summary = "서버 상태 확인", description = "서버가 정상 동작하는지 확인합니다.")
    public RsData<String> ping() {
        return RsData.successOf("pong");
    }

    @GetMapping("/info")
    @Operation(summary = "테스트 정보 조회", description = "테스트 환경 정보를 조회합니다.")
    public RsData<Map<String, Object>> getTestInfo() {
        Map<String, Object> info = testService.getTestInfo();
        return RsData.successOf(info);
    }

    @PostMapping("/data")
    @Operation(summary = "테스트 데이터 생성", description = "테스트용 데이터를 생성합니다.")
    public RsData<String> createTestData(@RequestParam(defaultValue = "5") int count) {
        String result = testService.createTestData(count);
        return RsData.successOf(result);
    }

    @GetMapping("/data")
    @Operation(summary = "테스트 데이터 조회", description = "생성된 테스트 데이터를 조회합니다.")
    public RsData<List<String>> getTestData() {
        List<String> data = testService.getTestData();
        return RsData.successOf(data);
    }

    @DeleteMapping("/data")
    @Operation(summary = "테스트 데이터 삭제", description = "모든 테스트 데이터를 삭제합니다.")
    public RsData<String> clearTestData() {
        String result = testService.clearTestData();
        return RsData.successOf(result);
    }
}