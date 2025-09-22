package com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiChatRoomRepository extends JpaRepository<AiChatRoom, Long> {
}
