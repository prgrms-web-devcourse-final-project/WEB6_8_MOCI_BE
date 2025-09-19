package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MentorChatMessage {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "room_id")
    private MentorChatRoom room;
    private boolean isAI;
}
