package com.moci_3d_backend.domain.chat.ai.aiChatMessage.controller;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto.AiChatMessageDto;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto.AiExchangeDto;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity.AiChatMessage;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.SenderType;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.service.AiChatMessageService;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "AiChatMessageController", description = "AI 메시지 관리 엔드포인트")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat/ai/rooms")
public class AiChatMessageController {
    private final AiChatMessageService aiChatMessageService;

    //TODO: 로그인한 사용자가없음

    public record CreateAiChatMessageReqBody(
            @NotNull Long roomId,
            @NotNull SenderType senderType,
            @NotBlank String content
    ) {
    }

    @Operation(summary = "AI 채팅방 메세지 생성",
            description = """
                    AI 채팅방 메세지를 생성합니다.
                    프론트에서 메세지보낼때 roomId를 같이 넘겨야합니다
                    """)
    @PostMapping("/{roomId}/messages")
    @Transactional
    public RsData<AiChatMessageDto> createAiChatMessage(@RequestBody @Valid CreateAiChatMessageReqBody reqBody) {
        AiChatMessage chatMessage = aiChatMessageService.create(reqBody.roomId, reqBody.senderType, reqBody.content);

        return new RsData<>(
                200, "%d번 메시지가 생성되었습니다.".formatted(chatMessage.getId()),
                new AiChatMessageDto(chatMessage)
        );
    }

    @Operation(summary = "AI 채팅방의 메시지 목록", description = "특정 방의 메세지목록")
    @GetMapping("/{roomId}/messages")
    public List<AiChatMessageDto> listByRoom(@PathVariable Long roomId) {

        List<AiChatMessage> messages = aiChatMessageService.listMessages(roomId);
        return messages.stream()
                .map(AiChatMessageDto::new)
                .toList();
    }


    public record MarkReadUpToRequest(
            @NotNull Long lastSeenMessageId
    ) {}

    @Operation(summary = "해당 방에서 lastSeenMessageId 까지 읽음 처리", description = "클라가 본 마지막 메시지 id를 보내면, 그 이하 메시지를 읽음 처리합니다.")
    @PatchMapping("/{roomId}/read")
    public RsData<List<AiChatMessageDto>> markAsRead(@PathVariable Long roomId,
                                                     @RequestBody @Valid MarkReadUpToRequest req) {
        List<AiChatMessageDto> dtoList = aiChatMessageService.markAsReadUpTo(roomId, req.lastSeenMessageId);

        return new RsData<>(
                200, "%d번 메세지까지 읽음 처리했습니다".formatted(req.lastSeenMessageId),
                dtoList
        );
    }

    public record AskAiRequest(
            @NotNull Long roomId,
            @NotBlank String content
    ) {}


    @Operation(summary = "사람이 메시지 보내고, AI 응답까지 한 번에 받기(동기)")
    @PostMapping("/{roomId}/ask")
    public RsData<AiExchangeDto> askAi(@RequestBody @Valid AskAiRequest req) {

        AiExchangeDto exchangeDto = aiChatMessageService.ask(req.roomId, req.content);
        return new RsData<>(
                200, "AI 응답을 받았습니다.",
                exchangeDto);

    }








}
