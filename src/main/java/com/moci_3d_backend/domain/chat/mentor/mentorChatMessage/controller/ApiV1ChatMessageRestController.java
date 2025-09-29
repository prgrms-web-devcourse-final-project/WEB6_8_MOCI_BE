package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatSendMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.rq.Rq;
import com.moci_3d_backend.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/mentor/message")
@RequiredArgsConstructor
@Tag(name="채팅 메시지", description = "채팅 메시지 중 HTTP 관련 API")
public class ApiV1ChatMessageRestController {

    private final MentorChatMessageService mentorChatMessageService;
    private final Rq rq;

    @GetMapping("{roomId}")
    @Operation(summary = "[모두] 채팅방의 메시지 불러오기", description = "채팅방에 접속했을 때 호출하면 이전 메시지를 불러옵니다.")
    public RsData<List<ChatSendMessage>> getMessages(
            @PathVariable(value = "roomId") Long roomId
    ){
        User user = rq.getActor();
        List<ChatSendMessage> chats = mentorChatMessageService.getMentorChatMessages(roomId, user);
        return RsData.of(200, "success to get messages", chats);
    }
}
