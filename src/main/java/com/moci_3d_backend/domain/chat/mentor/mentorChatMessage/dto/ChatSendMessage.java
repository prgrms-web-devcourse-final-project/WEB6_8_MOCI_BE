package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatSendMessage {
    private Long id;
    private String sender;
    private String content;
    private Long attachmentId;
    private LocalDateTime createdAt;

    public ChatSendMessage(Long id, String sender, String content, Long attachmentId, LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.attachmentId = attachmentId;
        this.createdAt = createdAt;
    }

    public ChatSendMessage(String sender, ChatReceiveMessage receiveMessage) {
        this.sender = sender;
        this.content = receiveMessage.getContent();
        this.attachmentId = receiveMessage.getAttachmentId();
    }
}
