package com.moci_3d_backend.domain.chat.ai.aiChatMessage.repository;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity.AiChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, Long> {

}
