package com.moci_3d_backend.externel.ai.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
// JSON 형태로 만들어 -> Gemini에게 보낼 요청
public class GeminiRequest {
    private List<Content> contents;

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
                .build();
    }
}
