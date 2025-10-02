package com.moci_3d_backend.domain.chat.ai.aiChatMessage.controller;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.service.AiChatMessageService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.external.ai.client.GeminiClient;
import com.moci_3d_backend.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1/chat/ai/rooms") //TODO: 나중에 api패스 바꿔야함
@Validated
@Tag(name = "AiChatStreamController", description = "AI 메시지 관리 엔드포인트2(스트리밍)")
@RequiredArgsConstructor
public class AiChatStreamController {

    private final AiChatMessageService aiChatMessageService;
    private final Rq rq;

    @Operation(summary = "사람이 메시지 보내고, AI 응답까지 한 번에 받기(비동기)",
            description = """
                    사람이 메시지를 보내고, AI의 응답을 스트리밍으로 받습니다.
                    1. 프론트에서는 roomId를 Path로 넘기고 content만 쿼리파라미터에 담아보냅니다.
                    2. AI의 응답을 스트리밍 시작
                    3. 프론트는 이벤트 받을 때마다 화면에 바로바로 출력
                    4. 스트리밍 종료후, 서버쪽에서 최종 AI 메세지를 DB에 저장
                    스트리밍 응답은 계속 이어지는 텍스트 스트림이지 완성된 JSON 객체가 아님 
                    """)
    @GetMapping(value = "{roomId}/ask-stream")
    public Flux<ServerSentEvent<String>> askAiStream(@PathVariable Long roomId,
                                                     @RequestParam String content) {
        User actor = rq.getActor();

        return aiChatMessageService.askStream(actor, roomId, content)
                .map(chunk -> ServerSentEvent.builder(chunk)
                        .event("delta")
                        .build())
                .startWith(ServerSentEvent.builder("connected").event("init").build());

    }
}
