package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatSendMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ApiV1ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MentorChatMessageService mentorChatMessageService;
    private final UserService userService;

    @MessageMapping("send")
    public void sendMessage(
            ChatReceiveMessage message,
            StompHeaderAccessor accessor,
            Principal principal
    ) {
        String roomIdStr = accessor.getFirstNativeHeader("roomId");
        if (roomIdStr == null){
            throw new IllegalArgumentException("roomId is null");
        }
        Long roomId = Long.parseLong(roomIdStr);
        SecurityUser securityUser = (SecurityUser) principal;
        User userRef = userService.getReferenceById(securityUser.getId());
        ChatSendMessage chatSendMessage = new ChatSendMessage(securityUser.getNickname(), message);
        mentorChatMessageService.saveMentorChatMessage(message, userRef, roomId);
        messagingTemplate.convertAndSend("/api/v1/chat/topic/%d".formatted(roomId), chatSendMessage);
    }
}
