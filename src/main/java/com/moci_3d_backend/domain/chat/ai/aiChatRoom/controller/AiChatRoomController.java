package com.moci_3d_backend.domain.chat.ai.aiChatRoom.controller;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.service.AiChatRoomService;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "AiChatRoomController", description = "AI 채팅방 관리 엔드포인트")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ai/chat_rooms")
public class AiChatRoomController {
    private final AiChatRoomService aiChatRoomService;


    public record CreateAiRoomReqBody(
            @NotBlank String title,
            @Size(max = 50) String category) {
    }

    @PostMapping
    public RsData<>
}
