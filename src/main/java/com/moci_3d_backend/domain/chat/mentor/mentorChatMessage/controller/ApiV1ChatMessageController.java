package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatSendMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ApiV1ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MentorChatMessageService mentorChatMessageService;

    @MessageMapping("send/{roomId}")
    public void sendMessage(
            ChatReceiveMessage message,
            @DestinationVariable(value = "roomId") Long roomId,
            Principal principal
    ) {
        SecurityUser securityUser = (SecurityUser) principal;
        ChatSendMessage chatSendMessage = new ChatSendMessage(securityUser.getNickname(), message);
        messagingTemplate.convertAndSend("/api/v1/chat/topic/%d".formatted(roomId), chatSendMessage);
    }
}
