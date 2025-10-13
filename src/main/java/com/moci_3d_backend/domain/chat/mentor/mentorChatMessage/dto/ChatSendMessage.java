package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ChatSendMessage {
    private Long id;
    private String sender;
    private String content;
    private Long attachmentId;
    private String attachmentUrl;
    private LocalDateTime createdAt;

    public ChatSendMessage(Long id, String sender, String content, Long attachmentId, LocalDateTime createdAt, String attachmentUrl) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.attachmentId = attachmentId;
        this.createdAt = createdAt;
        this.attachmentUrl = attachmentUrl;
    }

    public ChatSendMessage(String sender, ChatReceiveMessage receiveMessage) {
        this.id = -1L;
        this.sender = sender;
        this.content = receiveMessage.getContent();
        this.attachmentId = receiveMessage.getAttachmentId();
        this.attachmentUrl = "";
        this.createdAt = LocalDateTime.now();
    }
}
