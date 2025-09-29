package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DetailMentorChatRoom {
    private Long id;
    private String title;
    private String category;
    private Integer digital_level;
    private LocalDateTime createdAt;

    public DetailMentorChatRoom(MentorChatRoom mentorChatRoom){
        this.id = mentorChatRoom.getId();
        this.title = mentorChatRoom.getQuestion();
        this.category = mentorChatRoom.getCategory();
        this.digital_level = mentorChatRoom.getMentee().getDigitalLevel();
        this.createdAt = mentorChatRoom.getCreatedAt();
    }
}
