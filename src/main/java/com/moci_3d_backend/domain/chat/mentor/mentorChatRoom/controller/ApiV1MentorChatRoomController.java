package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.DetailMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chat/mentor/mentor/room")
@RequiredArgsConstructor
@Tag(name="멘토의 채팅방", description = "멘토의 채팅방 관련 API")
public class ApiV1MentorChatRoomController {
    private final MentorChatRoomService mentorChatRoomService;
    private final Rq rq;
    private final MentorChatMessageService mentorChatMessageService;

    @PutMapping("/join/{roomId}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 채팅방 입장", description = "멘토가 채팅방에 입장합니다.")
    public RsData<MentorChatRoomResponse> joinMentorChatRoom(
            @PathVariable(value = "roomId") Long roomId
    ){
        User user = rq.getActor();
        MentorChatRoomResponse response = mentorChatRoomService.joinMentorChatRoom(roomId, user);
        return RsData.of(200, "success to join mentor chat room", response);
    }

    @GetMapping("/my-mentees")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 입장했던 채팅방 조회", description = "멘토가 입장했던 채팅방을 조회합니다.")
    public RsData<List<MentorChatRoomResponse>> getMyMenteeChatRooms(){
        User user = rq.getActor();
        List<MentorChatRoomResponse> mentees = mentorChatRoomService.getMentorChatRooms(user);
        return RsData.of(200, "success to load my mentee chat rooms", mentees);
    }

    @GetMapping("/non-mentor-list")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 입장하지 않은 채팅방을 조회합니다.", description = "멘토가 입장하지 않은 채팅방을 조회합니다.")
    public RsData<List<DetailMentorChatRoom>> getMentorChatRooms(){
        List<DetailMentorChatRoom> chatRooms = mentorChatRoomService.getDetailMentorChatRooms();
        return RsData.of(200, "success to load chat rooms", chatRooms);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 전체 채팅방을 조회합니다.", description = "모든 채팅방을 조회합니다.")
    public RsData<List<DetailMentorChatRoom>> getAllChatRooms(){
        List<DetailMentorChatRoom> chatRooms = mentorChatRoomService.getAllMentorChatRooms();
        return RsData.of(200, "success to load chat rooms", chatRooms);
    }

    @PutMapping("/exit/{roomId}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 채팅방이 해결되었음 ")
    public RsData<Void> closeChatRoom(
            @PathVariable(value = "roomId") Long roomId
    ){
        ChatReceiveMessage chatreceiveMessage = new ChatReceiveMessage("멘토님이 채팅방을 나가셨습니다.", 0L);
        mentorChatMessageService.sendMessage(roomId, chatreceiveMessage, Optional.empty());
        mentorChatRoomService.setSolved(roomId);
        return RsData.of(200, "success to close chat room");
    }
}
