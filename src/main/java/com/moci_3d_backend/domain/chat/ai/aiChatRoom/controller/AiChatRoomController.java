package com.moci_3d_backend.domain.chat.ai.aiChatRoom.controller;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.dto.AiChatRoomDto;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.dto.AiChatRoomListDto;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.service.AiChatRoomService;
import com.moci_3d_backend.global.exception.ServiceException;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "AiChatRoomController", description = "AI 채팅방 관리 엔드포인트")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat/ai/room")
public class AiChatRoomController {
    private final AiChatRoomService aiChatRoomService;


    //TODO: 로그인한 사용자가 없다 추후에 추가한다.

    public record CreateAiRoomReqBody(
            @NotBlank String title,
            @Size(max = 50) String category) {
    }

    @Operation(summary = "AI 채팅방 생성", description = "AI 채팅방을 생성합니다.")
    @PostMapping
    @Transactional
    public RsData<AiChatRoomDto> createAiChatRoom(CreateAiRoomReqBody reqBody) {

        AiChatRoom chatRoom = aiChatRoomService.create(reqBody.title, reqBody.category);

        return new RsData<>(
                200, "%d번 AI 채팅방이 생성되었습니다.".formatted(chatRoom.getId()),
                new AiChatRoomDto(chatRoom)
        );

    }


    @Operation(summary = "AI 채팅방 단건 조회", description = "AI 채팅방 ID로 단건 조회합니다.")
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public RsData<AiChatRoomDto> getAiChatRoom(@PathVariable long id) {
        AiChatRoom aiChatRoom = aiChatRoomService.getRoom(id)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));

        AiChatRoomDto aiChatRoomDto = new AiChatRoomDto(aiChatRoom);

        return new RsData<>(
                200, "%d번 AI 채팅방 조회에 성공했습니다.".formatted(id),
                aiChatRoomDto
        );
    }

    @Operation( summary = "AI 채팅방 다건 조회", description = "AI 채탱방 모두 조회합니다.")
    @GetMapping
    @Transactional(readOnly = true)
    public AiChatRoomListDto getAiChatRooms() {
        List<AiChatRoom> aiChatRooms = aiChatRoomService.getRooms();

        List<AiChatRoomDto> roomDtos = aiChatRooms.stream()
                .map(AiChatRoomDto::new)
                .toList();

        return new AiChatRoomListDto(roomDtos, roomDtos.size());
    }
}
