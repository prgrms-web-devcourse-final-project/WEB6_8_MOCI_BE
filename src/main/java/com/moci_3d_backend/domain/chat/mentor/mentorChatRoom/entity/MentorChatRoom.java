package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.MentorChatMessage;
import com.moci_3d_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MentorChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="category",length = 255)
    private String category;

    @Column(name="sub_category" ,length = 255)
    private String subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = true)
    private User mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id")
    private User mentee;

    private boolean status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime matchedAt;

    private LocalDateTime lastMessageAt;

    private LocalDateTime mentorLastAt;

    private LocalDateTime menteeLastAt;

    @Setter
    private boolean deleted;

    @OneToMany(mappedBy = "room")
    List<MentorChatMessage> mentorChatMessageList;


    public MentorChatRoom(String category, String subCategory, User mentee){
        this.category = category;
        this.subCategory = subCategory;
        this.mentee = mentee;
        this.status = true;
        this.menteeLastAt = LocalDateTime.now();
        this.deleted = false;
    }

    public void joinMentor(User mentor){
        this.mentor = mentor;
        this.matchedAt = LocalDateTime.now();
        this.mentorLastAt = LocalDateTime.now();
    }

    public void updateLastMessageAt(){
        this.lastMessageAt = LocalDateTime.now();
    }
}
