package com.moci_3d_backend.domain.user.entity;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token", length = 50, unique = true)
    private String refreshToken;

    @Column(name = "user_id")
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

    @Column(name = "digital_level")
    private Integer digitalLevel;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Entity관계 매핑
    // 공개자료실
    @OneToMany(mappedBy = "uploadedBy", fetch = FetchType.LAZY)
    private List<PublicArchive> uploadedArchives;

//     멘토 참여 채팅방
     @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
     private List<MentorChatRoom> mentorChatRooms;


//     멘티 참여 채팅방
     @OneToMany(mappedBy = "mentee", fetch = FetchType.LAZY)
     private List<MentorChatRoom> menteeChatRooms;


//     AI 채팅방
     @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
     private List<AiChatRoom> aiChatRooms;

    // === 비즈니스 메서드 ===
    public void updateName(String name) {
        this.name = name;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateDigitalLevel(Integer digitalLevel) {
        this.digitalLevel = digitalLevel;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        authorities.add("ROLE_" + this.role.name());

        return authorities;
    }

    public enum UserRole {
        USER, ADMIN, MENTOR
    }
}
