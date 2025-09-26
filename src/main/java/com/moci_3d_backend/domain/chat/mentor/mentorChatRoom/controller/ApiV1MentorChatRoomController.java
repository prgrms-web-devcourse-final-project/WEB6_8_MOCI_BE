package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.DetailMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/mentor/mentor/room")
@RequiredArgsConstructor
@Tag(name="멘토의 채팅방", description = "멘토의 채팅방 관련 API")
public class ApiV1MentorChatRoomController {
    private final MentorChatRoomService mentorChatRoomService;

    @PostMapping("/join/{roomId}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 채팅방 입장", description = "멘토가 채팅방에 입장합니다.")
    public RsData<Void> joinMentorChatRoom(
            @PathVariable(value = "roomId") Long roomId,
            User user // 컴파일 에러 방지용
    ){
        // TODO 로그인한 회원의 정보를 가져와야함
        mentorChatRoomService.joinMentorChatRoom(roomId, user);
        return RsData.of(200, "success to join mentor chat room");
    }

    @GetMapping("/my-mentees")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "[멘토] 입장했던 채팅방 조회", description = "멘토가 입장했던 채팅방을 조회합니다.")
    public RsData<List<MentorChatRoomResponse>> getMyMenteeChatRooms(
            User user // 컴파일 에러 방지용
    ){
        // TODO 로그인한 회원의 정보를 가져와야함
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
}
