package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateMentorChatRoom {
    @NotBlank
    private String category;
    @NotBlank
    private String question;

    public CreateMentorChatRoom(String category, String question) {
        this.category = category;
        this.question = question;
    }
}
