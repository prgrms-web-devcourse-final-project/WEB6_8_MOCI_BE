package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.DetailMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mentor/chatroom")
@RequiredArgsConstructor
public class ApiV1MentorChatRoomController {
    private final MentorChatRoomService mentorChatRoomService;

    @PostMapping("/join/{roomId}")
    @PreAuthorize("hasRole('MENTOR')")
    public void joinMentorChatRoom(
            @PathVariable(value = "roomId") Long roomId,
            User user // 컴파일 에러 방지용
    ){
        // TODO 로그인한 회원의 정보를 가져와야함
        mentorChatRoomService.joinMentorChatRoom(roomId, user);
    }

    @GetMapping("/my-mentees")
    @PreAuthorize("hasRole('MENTOR')")
    public RsData<List<MentorChatRoomResponse>> getMyMenteeChatRooms(
            User user // 컴파일 에러 방지용
    ){
        var mentees = mentorChatRoomService.getMentorChatRooms(user);
        return RsData.of(200, "success to load my mentee chat rooms", mentees);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('MENTOR')")
    public RsData<List<DetailMentorChatRoom>> getMentorChatRooms(){
        var chatRooms = mentorChatRoomService.getDetailMentorChatRooms();
        return RsData.of(200, "success to load chat rooms", chatRooms);
    }
}
