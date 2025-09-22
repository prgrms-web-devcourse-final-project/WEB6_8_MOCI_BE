package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat/mentee/room")
@RequiredArgsConstructor
public class ApiV1MenteeChatRoomController {
    private final MentorChatRoomService mentorChatRoomService;

    @PostMapping()
    public RsData<Void> createMentorChatRoom(
            @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(value = "sub_category", defaultValue = "") String subCategory,
            User user // 컴파일 에러 방지용
    ) {
        // TODO 로그인한 회원의 정보를 가져와야함
        mentorChatRoomService.createMentorChatRoom(category, subCategory, user);
        return RsData.of(201, "success to create chat room");
    }

    @GetMapping()
    public RsData<Page<MentorChatRoomResponse>> getMentorChatRooms(
            @PageableDefault(size = 10, page = 0)Pageable pageable,
            @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(value = "sub_category", defaultValue = "") String subCategory,
            User user // 컴파일 에러 방지용
    ){
        // TODO 로그인한 회원의 정보를 가져와야함
        Page<MentorChatRoomResponse> mentorChatRoomResponsePage = mentorChatRoomService.getMentorChatRooms(user, pageable);
        return RsData.of(200, "success to get chat rooms", mentorChatRoomResponsePage);
    }

    @DeleteMapping("{room_id}")
    public RsData<Void> deleteMentorChatRoom(
            @PathVariable("room_id") Long roomId,
            User user // 컴파일 에러 방지용
    ){
        // TODO 로그인한 회원의 정보를 가져와야함
        mentorChatRoomService.deleteMentorChatRoom(roomId, user);
        return RsData.of(200, "success to delete chat room");
    }
}
