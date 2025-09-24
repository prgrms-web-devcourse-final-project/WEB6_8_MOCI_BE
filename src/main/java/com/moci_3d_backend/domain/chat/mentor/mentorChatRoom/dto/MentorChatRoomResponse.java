package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import lombok.Getter;

@Getter
public class MentorChatRoomResponse {
    private Long id;
    private String title;
    private Integer unread_count;

    public MentorChatRoomResponse(MentorChatRoom entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.unread_count = 0;
    }
    public MentorChatRoomResponse(Long id, String title, Integer unread_count){
        this.id = id;
        this.title = title;
        this.unread_count = unread_count;
    }
}
