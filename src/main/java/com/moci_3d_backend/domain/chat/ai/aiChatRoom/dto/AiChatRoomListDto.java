package com.moci_3d_backend.domain.chat.ai.aiChatRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AiChatRoomListDto {
    private List<AiChatRoomDto> rooms; //채팅방 목록
    private int totalRooms; //총 개수
}
