package com.moci_3d_backend.domain.chat.ai.aiChatRoom.controller;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.service.AiChatMessageService;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.dto.AiChatRoomDto;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.dto.AiChatRoomListDto;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.service.AiChatRoomService;
import com.moci_3d_backend.global.exception.ServiceException;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final AiChatMessageService aiChatMessageService;

    //TODO: 로그인한 사용자가 없다 추후에 추가한다.

    public record CreateAiRoomReqBody(
            @NotBlank String title) {
    }

    @Operation(summary = "AI 채팅방 생성", description = "질문을 입력하면 해당 질문을 제목으로 하는 AI 채팅방을 생성하고, 동시에 첫 질문을 등록합니다.")
    @PostMapping
    @Transactional
    public RsData<AiChatRoomDto> createAiChatRoom(@RequestBody @Valid CreateAiRoomReqBody reqBody) {

        AiChatRoom chatRoom = aiChatRoomService.create(reqBody.title);

        // 첫 질문을 등록하고 AI 응답을 받는 로직
        aiChatMessageService.ask(chatRoom.getId(), reqBody.title);

        return new RsData<>(
                200,
                "%d번 AI 채팅방이 생성되고 첫 질문이 등록되고 ai 응답을 받았습니다".formatted(chatRoom.getId()),
                new AiChatRoomDto(chatRoom)
        );


    }


    @Operation(summary = "AI 채팅방 단건 조회", description = "AI 채팅방 ID로 단건 조회합니다.")
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public RsData<AiChatRoomDto> getAiChatRoom(@PathVariable Long id) {
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
    public RsData<AiChatRoomListDto> getAiChatRooms() {
        List<AiChatRoom> aiChatRooms = aiChatRoomService.getRooms();

        List<AiChatRoomDto> roomDtos = aiChatRooms.stream()
                .map(AiChatRoomDto::new)
                .toList();

        return new RsData<>(
                200, "AI 채팅방 목록 조회에 성공했습니다.",
                new AiChatRoomListDto(roomDtos, roomDtos.size())
        );
    }

    @Operation( summary = "자기의 AI 채팅방 목록 조회", description = "자기자신의 채팅방 목록조회")
    @GetMapping("/mine")
    @Transactional(readOnly = true)
    public RsData<AiChatRoomListDto> getMyAiChatRooms(
            // TODO: 실제 구현시 @AuthenticationPrincipal 또는 SecurityContext에서 User 정보를 가져와야 함
    ) {
        //일단 유저가 없으니 주석
//        if(user == null) {
//            throw new ServiceException(401, "로그인이 필요합니다.");
//        }
//        Long userId = user.getId();

        List<AiChatRoom> aiChatRooms = aiChatRoomService.getMyRooms(1L); // TODO: 실제 사용자 ID로 변경

        List<AiChatRoomDto> roomDtos = aiChatRooms.stream()
                .map(AiChatRoomDto::new)
                .toList();

        return new RsData<>(
                200, "자신의 AI 채팅방 목록 조회에 성공했습니다.",
                new AiChatRoomListDto(roomDtos, roomDtos.size())
        );

    }

    @Operation( summary = "AI 채팅방을 삭제 ", description = "AI 채팅방을 삭제합니다.")
    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Void> deleteAiChatRoom(@PathVariable Long id) {// TODO: 실제 구현시 @AuthenticationPrincipal 또는 SecurityContext에서 User 정보를 가져와야 함
//        if(user == null ) {
//            throw new ServiceException(401, "로그인이 필요합니다.");
//        }
        aiChatRoomService.delete(id);

        return new RsData<>(
                200,"%d번 AI 채팅방이 삭제되었습니다.".formatted(id), null);
    }
}
