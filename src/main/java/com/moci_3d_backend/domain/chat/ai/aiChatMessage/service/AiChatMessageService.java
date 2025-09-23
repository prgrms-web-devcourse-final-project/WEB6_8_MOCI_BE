package com.moci_3d_backend.domain.chat.ai.aiChatMessage.service;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity.AiChatMessage;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.MessageStatus;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.SenderType;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.repository.AiChatMessageRepository;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import com.moci_3d_backend.global.exception.ServiceException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AiChatMessageService {
    private final AiChatRoomRepository aiChatRoomRepository;
    private final AiChatMessageRepository aiChatMessageRepository;


    public AiChatMessage create(Long roomId, SenderType senderType, String content) {
        AiChatRoom room = aiChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));


        AiChatMessage message = AiChatMessage.builder()
                .room(room)
                .senderType(senderType)
                .content(content)
                .status(MessageStatus.전달완료)
                .build();
        aiChatMessageRepository.save(message);

        room.updateLastMessageAt(LocalDateTime.now());
        return message;
    }
}
