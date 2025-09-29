package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto;

import lombok.Getter;

@Getter
public class ChatReceiveMessage {
    private String content;
    private Long attachmentId;
}
