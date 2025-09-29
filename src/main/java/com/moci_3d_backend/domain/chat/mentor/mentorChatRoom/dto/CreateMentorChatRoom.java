package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto;

import lombok.Getter;

@Getter
public class CreateMentorChatRoom {
    private String category;
    private String question;

    public CreateMentorChatRoom(String category, String question) {
        this.category = category;
        this.question = question;
    }
}
