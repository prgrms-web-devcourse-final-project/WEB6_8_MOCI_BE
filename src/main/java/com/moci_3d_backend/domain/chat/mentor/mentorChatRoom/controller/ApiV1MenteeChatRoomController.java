package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.CreateMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MenteeChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/mentor/mentee/room")
@RequiredArgsConstructor
@Tag(name="멘티의 채팅방", description = "멘티의 채팅방 관련 API")
public class ApiV1MenteeChatRoomController {
    private final MenteeChatRoomService menteeChatRoomService;
    private final Rq rq;

    @PostMapping()
    @Operation(summary = "[멘티] 채팅방 생성", description = "멘티가 채팅방을 생성합니다.")
    public RsData<Void> createMentorChatRoom(
            @RequestBody CreateMentorChatRoom createMentorChatRoom
    ) {
        User user = rq.getActor();
        menteeChatRoomService.createMenteeChatRoom(createMentorChatRoom, user);
        return RsData.of(201, "success to create chat room");
    }

    @GetMapping()
    @Operation(summary = "[멘티] 채팅방을 조회합니다.", description = "멘티가 참여한 채팅방을 조회합니다.")
    public RsData<List<MentorChatRoomResponse>> getMenteeChatRooms(){
        User user = rq.getActor();
        List<MentorChatRoomResponse> mentorChatRoomResponsePage = menteeChatRoomService.getMenteeChatRooms(user);
        return RsData.of(200, "success to get chat rooms", mentorChatRoomResponsePage);
    }

    @DeleteMapping("{room_id}")
    @Operation(summary = "[멘티] 채팅방을 나갑니다.", description = "멘티가 참여한 채팅방을 나갑니다.")
    public RsData<Void> deleteMenteeChatRoom(
            @PathVariable("room_id") Long roomId
    ){
        User user = rq.getActor();
        menteeChatRoomService.deleteMenteeChatRoom(roomId, user);
        return RsData.of(200, "success to delete chat room");
    }
}
