package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import lombok.Getter;

@Getter
public class MentorChatRoomResponse {
    private Long id;
    private String title; // Chat Message 구현 후 추가

    public MentorChatRoomResponse(MentorChatRoom entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
    }
}
