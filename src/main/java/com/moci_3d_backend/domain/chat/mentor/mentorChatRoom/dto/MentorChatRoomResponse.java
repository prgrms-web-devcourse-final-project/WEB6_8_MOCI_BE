package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDateTime;

@Getter
public class MentorChatRoomResponse {
    private Long id;
    private String category;
    private String question;
    private Long unread_count;
    private Boolean mentee_left;
    private Boolean mentor_left;
    private LocalDateTime createdAt;

    public MentorChatRoomResponse(MentorChatRoom entity){
        this.id = entity.getId();
        this.category = entity.getCategory();
        this.question = entity.getQuestion();
        this.unread_count = 0L;
        this.mentee_left = entity.isMenteeLeft();
        this.mentor_left = entity.isMentorLeft();
        this.createdAt = entity.getCreatedAt();
    }
    @QueryProjection
    public MentorChatRoomResponse(Long id, String category, String question, Long unread_count, LocalDateTime createdAt, Boolean mentee_left, Boolean mentor_left){
        this.id = id;
        this.category = category;
        this.question = question;
        this.unread_count = unread_count;
        this.createdAt = createdAt;
        this.mentee_left = mentee_left;
        this.mentor_left = mentor_left;
    }
}
