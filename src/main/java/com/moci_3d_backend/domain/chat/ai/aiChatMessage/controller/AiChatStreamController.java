package com.moci_3d_backend.domain.chat.ai.aiChatMessage.controller;

import com.moci_3d_backend.external.ai.client.GeminiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/chat/ai/stream")
@Validated
@Tag(name = "AiChatStreamController", description = "AI 메시지 관리 엔드포인트2(스트리밍)")
@RequiredArgsConstructor
public class AiChatStreamController {

    private final GeminiClient geminiClient;

    public record AskAiStreamRequest(
            @NotBlank String content
    ) { }

    @Operation(summary = "사람이 메시지 보내고, AI 응답까지 한 번에 받기(비동기)",
            description = """
                    사람이 메시지를 보내고, AI의 응답을 스트리밍으로 받습니다.
                    URL 경로 변수로 roomId를 받고,
                    프론트에서는 roomId를 Path로 넘기고 content만 body에 담아보냅니다.
                    """)
    @GetMapping(value = "{roomId}/ask-stream")
    public Flux<ServerSentEvent<String>> askAiStream(@RequestBody AskAiStreamRequest request) {
        return geminiClient.streamChatResponse(request.content())
                .map(chunk -> ServerSentEvent.builder(chunk)
                        .event("delta")
                        .build())
                .startWith(ServerSentEvent.builder("connected").event("init").build());
    }
}
