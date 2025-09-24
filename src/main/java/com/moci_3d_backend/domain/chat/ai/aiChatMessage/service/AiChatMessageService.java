package com.moci_3d_backend.domain.chat.ai.aiChatMessage.service;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto.AiChatMessageDto;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto.AiExchangeDto;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity.AiChatMessage;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.MessageStatus;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.SenderType;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.repository.AiChatMessageRepository;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import com.moci_3d_backend.external.ai.client.GeminiClient;
import com.moci_3d_backend.external.ai.dto.GeminiRequest;
import com.moci_3d_backend.external.ai.dto.GeminiResponse;
import com.moci_3d_backend.global.exception.ServiceException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiChatMessageService {
    private final AiChatRoomRepository aiChatRoomRepository;
    private final AiChatMessageRepository aiChatMessageRepository;
    private final GeminiClient geminiClient;


    public AiChatMessage create(Long roomId, SenderType senderType, String content) {
        AiChatRoom room = aiChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));


        AiChatMessage message = AiChatMessage.builder()
                .room(room)
                .senderType(senderType)
                .content(content)
                .status(MessageStatus.DELIVERED)
                .build();
        aiChatMessageRepository.save(message);

        room.updateLastMessageAt(LocalDateTime.now());
        return message;
    }

    @Transactional
    public AiExchangeDto ask(Long roomId, String content) {
        // 사용자 메시지 저장
        AiChatMessage userMessage = create(roomId, SenderType.HUMAN, content);

        // 히스토리 프롬프트 구성
        List<AiChatMessage> history = aiChatMessageRepository.findByRoomIdOrderByIdAsc(roomId);
        String prompt = buildPrompt(history);

        // AI 호출
        String aiText = geminiClient.generateChatResponse(prompt);

        // Ai 메시지 저장
        AiChatMessage aiMessage = create(roomId, SenderType.AI, aiText);

        return new AiExchangeDto(
                new AiChatMessageDto(userMessage),
                new AiChatMessageDto(aiMessage)
        );
    }


    private String buildPrompt(List<AiChatMessage> history) {
        //직전 대화 N개로 프롬프트 구성

        final int DEFAULT_CONTEXT_SIZE = 10;

        int N = Math.max(0, history.size()-DEFAULT_CONTEXT_SIZE);
        List<AiChatMessage> last = history.subList(N, history.size());

        String hist = last.stream()
                .map(m -> (m.getSenderType() == SenderType.HUMAN ? "Human: " : "AI: ") + m.getContent())
                .collect(Collectors.joining("\n"));

        String system = """
                너는 친절한 한국인 AI 비서야.
                사용자가 50대 노인이라고 생각하고 밝고 명확하게 한국어로 대답해줘.
                """;

        return system + "\n\n" + hist + "\n\nAI:";
    }



    @Transactional(readOnly = true)
    public List<AiChatMessage> listMessages(Long roomId) {
        AiChatRoom room = aiChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));

        return aiChatMessageRepository.findByRoomIdOrderByIdAsc(roomId);

    }

    @Transactional
    public AiChatMessage read(Long messageId, LocalDateTime readAt) {
        AiChatMessage message = aiChatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 메시지입니다."));

        message.markRead(readAt != null ? readAt : LocalDateTime.now());

        return aiChatMessageRepository.save(message);
    }

    @Transactional
    public List<AiChatMessageDto> markAsReadUpTo(Long roomId, Long lastSeenMessageId) {
        AiChatMessage last = aiChatMessageRepository.findById(lastSeenMessageId)
                .orElseThrow(()-> new ServiceException(404,"lastSeenMessageId가 존재하지 않습니다."));
        if(!last.getRoom().getId().equals(roomId)) {
            throw new ServiceException(400, "해당 메세지는 요청한 roomId에 속하지 않습니다.");
        }

        List<AiChatMessage> toMark = aiChatMessageRepository.findByRoomIdAndIdLessThanEqualAndStatusNot(
                roomId, lastSeenMessageId, MessageStatus.READ
        );

        return toMark.stream()
                .map(m -> read(m.getId(), null))
                .map(AiChatMessageDto::new)
                .collect(Collectors.toList());
    }
}
