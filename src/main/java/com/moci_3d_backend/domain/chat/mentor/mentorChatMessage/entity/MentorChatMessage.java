package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.user.entity.User;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;
    @Column(columnDefinition = "TEXT")
    private String content;
    private boolean isAI;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="attachment_id", nullable = true)
    private FileUpload attachment;

    public MentorChatMessage(MentorChatRoom room, User sender, String content, FileUpload attachment){
        this.room = room;
        this.sender = sender;
        this.content = content;
        this.attachment = attachment;
        this.isAI = false;
    }
}
