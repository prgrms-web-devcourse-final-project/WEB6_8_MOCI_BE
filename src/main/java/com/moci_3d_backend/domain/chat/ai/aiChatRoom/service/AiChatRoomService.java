package com.moci_3d_backend.domain.chat.ai.aiChatRoom.service;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import com.moci_3d_backend.domain.user.entity.User;
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

    public AiChatRoom create(User actor, String title) {
        AiChatRoom aiChatRoom = AiChatRoom.builder()
                .user(actor)
                .title(title)
                .status(true)
                .build();

        return aiChatRoomRepository.save(aiChatRoom);

    }

    public Optional<AiChatRoom> getRoom(User actor, long id) {

        AiChatRoom aiChatRoom = aiChatRoomRepository.findById(id)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));

        // 관리자면 바로 통과
        if (actor.getRole().equals(User.UserRole.ADMIN)) {
            return Optional.of(aiChatRoom);
        }

        // 본인 소유 방인지 체크
        if (!aiChatRoom.getUser().getId().equals(actor.getId())) {
            throw new ServiceException(403, "해당 채팅방에 접근할 권한이 없습니다.");
        }

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

    public void delete(long id, User actor) {

        AiChatRoom aiChatRoom = aiChatRoomRepository.findById(id)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));

        //권한 체크
        if(!actor.getRole().equals(User.UserRole.ADMIN) && !aiChatRoom.getUser().getId().equals(actor.getId())) {
            throw new ServiceException(403, "권한이 없습니다.");
        }

        aiChatRoomRepository.delete(aiChatRoom);
    }
}
