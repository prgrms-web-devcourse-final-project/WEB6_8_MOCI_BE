package com.moci_3d_backend.domain.chat.ai.aiChatRoom.controller;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.dto.AiChatRoomDto;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.service.AiChatRoomService;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "AiChatRoomController", description = "AI 채팅방 관리 엔드포인트")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat/ai/room")
public class AiChatRoomController {
    private final AiChatRoomService aiChatRoomService;


    public record CreateAiRoomReqBody(
            @NotBlank String title,
            @Size(max = 50) String category) {
    }

    @PostMapping
    @Transactional
    public RsData<AiChatRoomDto> createAiChatRoom(CreateAiRoomReqBody reqBody) {

        AiChatRoom chatRoom = aiChatRoomService.create(reqBody.title, reqBody.category);

        return new RsData<>(
                200,"%d번 AI 채팅방이 생성되었습니다.".formatted(chatRoom.getId()),
                new AiChatRoomDto(chatRoom)
        );

    }
}
