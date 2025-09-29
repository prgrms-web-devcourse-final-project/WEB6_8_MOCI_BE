package com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity.AiChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AiChatMessageDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private Long roomId;
    private Long senderId; //사람이면 User, AI면 null
    private String senderType;
    private String content;
    private String status;

    public AiChatMessageDto(AiChatMessage message) {
        this.id = message.getId();
        this.createdAt = message.getCreatedAt();
        this.readAt = message.getReadAt();
        this.roomId = message.getRoom().getId();
        this.senderId = message.getSender() != null ? message.getSender().getId() : null;
        this.senderType = message.getSenderType().name();
        this.content = message.getContent();
        this.status = message.getStatus().name();
    }

}
