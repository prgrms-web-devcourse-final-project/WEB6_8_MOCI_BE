package com.moci_3d_backend.domain.chat.ai.aiChatRoom.service;

import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatRoomService {
    private  final AiChatRoomRepository aiChatRoomRepository;

}
