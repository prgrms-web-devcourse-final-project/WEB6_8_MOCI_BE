package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ApiV1ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MentorChatMessageService mentorChatMessageService;

    @MessageMapping("send/{roomId}")
    public void sendMessage(
            ChatReceiveMessage message,
            @DestinationVariable(value = "roomId") Long roomId
//            ,Principal principal
    ) {
        // TODO Principal을 통해 User 객체를 얻어와야 함
        messagingTemplate.convertAndSend("/api/v1/chat/topic/%d".formatted(roomId), message);
    }
}
