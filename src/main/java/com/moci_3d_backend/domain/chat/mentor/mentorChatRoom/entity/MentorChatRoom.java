package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.MentorChatMessage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class MentorChatRoom {
    @Id
    private Long id;
    @Column(name="category",length = 255)
    private String category;
    @Column(name="sub_category" ,length = 255)
    private String subCategory;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "mentor_id")
//    private User mentor;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "mentee_id")
//    private User mentee;
    private boolean status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime matchedAt;
    private LocalDateTime lastMessageAt;
    private LocalDateTime mentorLastAt;
    private LocalDateTime menteeLastAt;

    @OneToMany(mappedBy = "room")
    List<MentorChatMessage> mentorChatMessageList;

}
