package com.moci_3d_backend.domain.chat.ai.aiChatMessage.controller;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto.AiChatMessageDto;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity.AiChatMessage;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.SenderType;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.service.AiChatMessageService;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.service.AiChatRoomService;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "AiChatMessageController", description = "AI 메시지 관리 엔드포인트")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat/ai/message")
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
    @PostMapping
    @Transactional
    public RsData<AiChatMessageDto> createAiChatMessage(@RequestBody @Valid CreateAiChatMessageReqBody reqBody) {
        AiChatMessage chatMessage = aiChatMessageService.create(reqBody.roomId, reqBody.senderType, reqBody.content);

        return new RsData<>(
                200, "%d번 메시지가 생성되었습니다.".formatted(chatMessage.getId()),
                new AiChatMessageDto(chatMessage)
        );
    }

}
