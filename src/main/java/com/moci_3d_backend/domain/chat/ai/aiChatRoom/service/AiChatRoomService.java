package com.moci_3d_backend.domain.chat.ai.aiChatRoom.service;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import com.moci_3d_backend.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatRoomService {
    private  final AiChatRoomRepository aiChatRoomRepository;

    public AiChatRoom create(String title) {
        AiChatRoom aiChatRoom = AiChatRoom.builder()
                .title(title)
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

    public List<AiChatRoom> getMyRooms(Long userId) {

        List<AiChatRoom> aiChatRooms = aiChatRoomRepository.findByUserIdOrderByLastMessageAtDesc(userId);

        log.info("자기 자신의 AI 채팅방 조회 성공 총 {}개 조회함 ",aiChatRooms.size());

        return aiChatRooms;
    }

    public void delete(long id) {

        AiChatRoom aiChatRoom = aiChatRoomRepository.findById(id)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));

        aiChatRoomRepository.delete(aiChatRoom);
    }
}
