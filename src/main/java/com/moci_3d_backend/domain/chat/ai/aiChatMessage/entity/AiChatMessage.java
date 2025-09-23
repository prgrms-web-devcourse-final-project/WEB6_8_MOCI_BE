package com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.MessageStatus;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.SenderType;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "ai_chat_message")
public class AiChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    private Long id;

    @CreatedDate
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime createdAt;

    private LocalDateTime readAt; //메시지 읽은 시간

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private AiChatRoom room;

//    @ManyToOne(fetch =  FetchType.LAZY)
//    @JoinColumn(name = "user_id") //사람이면 User, AI면 null
//    private User sender;
// 추후에 추가합니다 (유저와의 관계)

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", length = 20, nullable = false)
    private SenderType senderType; // SENDER, RECEIVER

    @Lob
    private String content; //메시지 내용

    private Long attachmentId; //첨부파일 ID (추후에 추가할 수도 있어서 남겨둠)

    private MessageStatus status; //메시지 상태 (예: SENT, DELIVERED, READ)

    public void markRead(LocalDateTime time) {
        this.status = MessageStatus.READ;
        this.readAt = time;
    }

}
