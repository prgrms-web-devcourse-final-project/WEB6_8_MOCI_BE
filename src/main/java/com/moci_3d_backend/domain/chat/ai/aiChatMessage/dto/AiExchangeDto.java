package com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AiExchangeDto {
    AiChatMessageDto userMessage;
    AiChatMessageDto aiMessage;

    public AiExchangeDto(AiChatMessageDto userMessage, AiChatMessageDto aiMessage) {
        this.userMessage = userMessage;
        this.aiMessage = aiMessage;
    }
}

