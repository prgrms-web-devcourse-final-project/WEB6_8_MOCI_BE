package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.DetailMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/mentor/admin/room")
@RequiredArgsConstructor
@Tag(name="어드민이 관리하는 멘토 채팅방", description = "어드민이 관리하는 멘토 채팅방 관련 API")
public class ApiV1AdminChatRoomController {
    private final MentorChatRoomService mentorChatRoomService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[어드민] 전체 채팅방을 조회합니다.", description = "모든 채팅방을 조회합니다.")
    public RsData<List<DetailMentorChatRoom>> getAllChatRooms(){
        List<DetailMentorChatRoom> chatRooms = mentorChatRoomService.getAllMentorChatRooms();
        return RsData.of(200, "success to load chat rooms", chatRooms);
    }
}
