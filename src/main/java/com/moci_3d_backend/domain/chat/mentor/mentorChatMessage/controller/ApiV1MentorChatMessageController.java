package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ApiV1MentorChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MentorChatMessageService mentorChatMessageService;

    @MessageMapping("send/{roomId}")
    public void sendMessage(
            @DestinationVariable(value = "roomId") Long roomId,
            ChatMessage message,
            Principal principal,
            User user // 컴파일 에러 방지용
    ) {
        // TODO Principal을 통해 User 객체를 얻어와야 함
        messagingTemplate.convertAndSend("/api/v1/chat/topic/%d".formatted(roomId), message);

    }
}
