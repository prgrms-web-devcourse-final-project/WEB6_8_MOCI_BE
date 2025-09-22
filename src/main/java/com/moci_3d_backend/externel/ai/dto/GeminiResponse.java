package com.moci_3d_backend.externel.ai.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
//재미나이로 부터 받은 JSON형식 -> Java 형식으로 매핑하기 위한 구조
public class GeminiResponse {
    private List<Candidate> candidates;

    @Getter
    @NoArgsConstructor
    public static class Candidate {
        private Content content;
    }

    @Getter
    @NoArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    @NoArgsConstructor
    public static class Part {
        private String text;
    }

    public String getGeneratedText() { //만약 응답이 1개 이상일 경우 첫번쨰 응답만 반환
        if (candidates != null && !candidates.isEmpty() &&
                candidates.getFirst().content != null &&
                candidates.getFirst().content.parts != null &&
                !candidates.getFirst().content.parts.isEmpty()) {
            return candidates.getFirst().content.parts.getFirst().text;
        }
        return "";
    }
}
