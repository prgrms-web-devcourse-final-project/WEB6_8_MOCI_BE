package com.moci_3d_backend.domain.chat.ai.aiChatMessage.repository;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity.AiChatMessage;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, Long> {

    List<AiChatMessage> findByRoomIdOrderByIdAsc(Long roomId);
    List<AiChatMessage> findByRoomIdAndIdLessThanEqualAndStatusNot(Long roomId, Long lastSeenMessageId, MessageStatus messageStatus);
    List<AiChatMessage> findByRoomIdAndContentLikeOrderByIdAsc(Long roomId, String query);
}
