package com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatRoomRepository extends JpaRepository<AiChatRoom, Long> {

    List<AiChatRoom> findByUserIdOrderByLastMessageAtDesc(Long userId);
    List<AiChatRoom> findByUserId(Long id);
}
