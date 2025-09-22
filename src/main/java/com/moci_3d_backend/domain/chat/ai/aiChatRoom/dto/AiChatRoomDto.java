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
    //private Long userId; 나중에 추가

    private String title;
    private boolean status;

    private String category;

    public AiChatRoomDto(AiChatRoom aiChatRoom) {
        this.id = aiChatRoom.getId();
        this.createdAt = aiChatRoom.getCreatedAt();
        this.modifiedAt = aiChatRoom.getModifiedAt();
        this.lastMessageAt = aiChatRoom.getLastMessageAt();
        //this.userId = aiChatRoom.getUser().getId();
        this.title = aiChatRoom.getTitle();
        this.status = aiChatRoom.isStatus();
        this.category = aiChatRoom.getCategory();

    }
}
