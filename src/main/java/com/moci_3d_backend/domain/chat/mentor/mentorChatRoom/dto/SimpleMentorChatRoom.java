package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import lombok.Getter;

@Getter
public class SimpleMentorChatRoom {
    private Long id;
    private String category;
    private String question;

    public SimpleMentorChatRoom(MentorChatRoom entity){
        this.id = entity.getId();
        this.category = entity.getCategory();
        this.question = entity.getQuestion();
    }
}
