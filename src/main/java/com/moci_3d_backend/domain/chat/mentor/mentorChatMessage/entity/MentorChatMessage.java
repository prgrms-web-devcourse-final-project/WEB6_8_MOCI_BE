package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MentorChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private MentorChatRoom room;
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "sender_id")
//    private User sender;
    @Column(columnDefinition = "TEXT")
    private String content;
    private boolean isAI;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="attachment_id")
//    private File attachment;
}
