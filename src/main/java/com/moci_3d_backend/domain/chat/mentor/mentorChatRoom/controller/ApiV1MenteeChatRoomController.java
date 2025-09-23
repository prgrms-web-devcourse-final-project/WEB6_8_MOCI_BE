package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.CreateMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MenteeChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/mentee/room")
@RequiredArgsConstructor
public class ApiV1MenteeChatRoomController {
    private final MenteeChatRoomService menteeChatRoomService;

    @PostMapping()
    public RsData<Void> createMentorChatRoom(
            @RequestBody CreateMentorChatRoom createMentorChatRoom,
            User user // 컴파일 에러 방지용
    ) {
        // TODO 로그인한 회원의 정보를 가져와야함
        menteeChatRoomService.createMenteeChatRoom(createMentorChatRoom, user);
        return RsData.of(201, "success to create chat room");
    }

    @GetMapping()
    public RsData<List<MentorChatRoomResponse>> getMentorChatRooms(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            User user // 컴파일 에러 방지용
    ){
        // TODO 로그인한 회원의 정보를 가져와야함
        List<MentorChatRoomResponse> mentorChatRoomResponsePage = menteeChatRoomService.getMenteeChatRooms(user);
        return RsData.of(200, "success to get chat rooms", mentorChatRoomResponsePage);
    }

    @DeleteMapping("{room_id}")
    public RsData<Void> deleteMentorChatRoom(
            @PathVariable("room_id") Long roomId,
            User user // 컴파일 에러 방지용
    ){
        // TODO 로그인한 회원의 정보를 가져와야함
        menteeChatRoomService.deleteMenteeChatRoom(roomId, user);
        return RsData.of(200, "success to delete chat room");
    }
}
