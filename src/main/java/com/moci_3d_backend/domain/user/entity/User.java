package com.moci_3d_backend.domain.user.entity;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
// import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
// import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 11)
    private String userId;

    @Column(name = "social_id", length = 50)
    private String socialId;

    @Column(name = "login_type", length = 20)
    private String loginType;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "email", length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "VARCHAR(20) DEFAULT 'USER'")
    private UserRole role;

    @Column(name = "digital_level", columnDefinition = "INTEGER DEFAULT 0")
    private Integer digitalLevel;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Entity관계 매핑
    // 공개지료실
    @OneToMany(mappedBy = "uploadedBy", fetch = FetchType.LAZY)
    private List<PublicArchive> uploadedArchives;
    
    // 멘토 참여 채팅방  
    // @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    // private List<MentorChatRoom> mentorChatRooms;
    
    
    // 멘티 참여 채팅방
    // @OneToMany(mappedBy = "mentee", fetch = FetchType.LAZY)
    // private List<MentorChatRoom> menteeChatRooms;
    
    
    // AI 채팅방 
    // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    // private List<AiChatRoom> aiChatRooms;

    public enum UserRole {
        USER, ADMIN, MENTOR
    }

}


