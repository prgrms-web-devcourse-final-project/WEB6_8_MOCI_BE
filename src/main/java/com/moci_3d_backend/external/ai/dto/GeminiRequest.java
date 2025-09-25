package com.moci_3d_backend.external.ai.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
// JSON 형태로 만들어 -> Gemini에게 보낼 요청
public class GeminiRequest {
    private List<Content> contents;
    private Map<String, Object> generationConfig;

    @Getter
    @Builder
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @Builder
    public static class Part {
        private String text;
    }

    public static GeminiRequest of(String prompt) {
        return GeminiRequest.builder()
                .contents(List.of(
                        Content.builder()
                                .parts(List.of(
                                        Part.builder()
                                                .text(prompt)
                                                .build()
                                ))
                                .build()
                ))
                .generationConfig(Map.of(   // 출력 설정 추가
                        "maxOutputTokens", 512,
                        "temperature", 0.4
                ))
                .build();
    }
}
