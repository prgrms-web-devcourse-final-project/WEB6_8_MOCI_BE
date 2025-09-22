package com.moci_3d_backend.domain.chat.ai.aiChatRoom.service;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiChatRoomService {
    private  final AiChatRoomRepository aiChatRoomRepository;

    public AiChatRoom create(String title,String category) {
        AiChatRoom aiChatRoom = AiChatRoom.builder()
                .title(title)
                .category(category)
                .status(true)
                .build();

        return aiChatRoomRepository.save(aiChatRoom);

    }

    public Optional<AiChatRoom> getRoom(long id) {
        return aiChatRoomRepository.findById(id);
    }

    public List<AiChatRoom> getRooms() {
        return aiChatRoomRepository.findAll();
    }
}
