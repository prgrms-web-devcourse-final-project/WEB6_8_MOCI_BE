package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatReceiveMessage {
    private String content;
    private Long attachmentId;

    public ChatReceiveMessage(String content, Long attachmentId) {
        this.content = content;
        this.attachmentId = attachmentId;
    }
}
