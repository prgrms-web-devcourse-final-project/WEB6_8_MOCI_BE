package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatSendMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ApiV1ChatMessageRestController {

    private final MentorChatMessageService mentorChatMessageService;

    @GetMapping("{roomId}")
    public RsData<Void> getMessages(
            @PathVariable(value = "roomId") Long roomId,
            User user
    ){
        List<ChatSendMessage> chats = mentorChatMessageService.getMentorChatMessages(roomId, user);
        return RsData.of(200, "success to get messages", null);
    }
}
