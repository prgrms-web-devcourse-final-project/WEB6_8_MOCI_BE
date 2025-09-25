package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto;

import lombok.Getter;

@Getter
public class ChatSendMessage {
    private String sender;
    private String content;
    private Long attachmentId;

    public ChatSendMessage(String sender, String content, Long attachmentId) {
        this.sender = sender;
        this.content = content;
        this.attachmentId = attachmentId;
    }
}
