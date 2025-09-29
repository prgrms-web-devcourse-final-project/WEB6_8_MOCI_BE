package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MentorChatRoomResponse {
    private Long id;
    private String question;
    private Long unread_count;

    public MentorChatRoomResponse(MentorChatRoom entity){
        this.id = entity.getId();
        this.question = entity.getQuestion();
        this.unread_count = 0L;
    }
    @QueryProjection
    public MentorChatRoomResponse(Long id, String question, Long unread_count){
        this.id = id;
        this.question = question;
        this.unread_count = unread_count;
    }
}
