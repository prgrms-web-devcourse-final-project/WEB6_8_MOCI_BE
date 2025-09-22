package com.moci_3d_backend.domain.chat.ai.aiChatRoom.dto;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AiChatRoomDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime lastMessageAt;
    private Long userId;

    private String title;
    private boolean status;

    private String category;

    public AiChatRoomDto(AiChatRoom aiChatRoom) {

    }
}
