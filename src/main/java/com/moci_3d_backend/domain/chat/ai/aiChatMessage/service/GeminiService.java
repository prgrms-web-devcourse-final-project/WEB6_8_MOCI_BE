package com.moci_3d_backend.domain.chat.ai.aiChatMessage.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final WebClient geminiWebClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.stream-url}")
    private String streamUrl;

    public Flux<String> streamAnswer(String content) {
        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", content))
                ))
        );

        return geminiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(streamUrl)     // 전체 URL 그대로 사용
                        .queryParam("key", apiKey)
                        .build())
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(JsonNode.class)
                .map(json -> json.at("/candidates/0/content/parts/0/text").asText(""))
                .filter(text -> !text.isBlank());
    }
}
