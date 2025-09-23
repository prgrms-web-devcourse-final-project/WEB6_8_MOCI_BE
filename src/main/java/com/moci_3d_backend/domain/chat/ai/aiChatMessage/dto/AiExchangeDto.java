package com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto;

public class AiExchangeDto {
    AiChatMessageDto userMessage;
    AiChatMessageDto aiMessage;

    public AiExchangeDto(AiChatMessageDto userMessage, AiChatMessageDto aiMessage) {
        this.userMessage = userMessage;
        this.aiMessage = aiMessage;
    }
}

