package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ChatSendMessage {
    private Long id;
    private String sender;
    private String senderRole;
    private String content;
    private Long attachmentId;
    private String attachmentUrl;
    private LocalDateTime createdAt;

    public ChatSendMessage(Long id, String sender, String senderRole, String content, Long attachmentId,String attachmentUrl ,LocalDateTime createdAt) {
        this.id = id;
        this.sender = sender;
        this.senderRole = senderRole;
        this.content = content;
        this.attachmentId = attachmentId;
        this.attachmentUrl = attachmentUrl;
        this.createdAt = createdAt;
    }

    /**
     * System이 전송할 용도로 사용됩니다.
     * @param sender
     * @param receiveMessage
     */
    public ChatSendMessage(String sender, ChatReceiveMessage receiveMessage) {
        this.id = -1L;
        this.sender = sender;
        this.senderRole = sender;
        this.content = receiveMessage.getContent();
        this.attachmentId = receiveMessage.getAttachmentId();
        this.attachmentUrl = "";
        this.createdAt = LocalDateTime.now();
    }
}
