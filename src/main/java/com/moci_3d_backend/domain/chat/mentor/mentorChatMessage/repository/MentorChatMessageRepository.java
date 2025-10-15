package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.repository;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.MentorChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorChatMessageRepository extends JpaRepository<MentorChatMessage, Long> {
    List<MentorChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);

    Long countByRoomId(Long roomId);
}
