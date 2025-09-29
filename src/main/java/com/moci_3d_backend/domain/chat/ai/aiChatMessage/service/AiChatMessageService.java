package com.moci_3d_backend.domain.chat.ai.aiChatMessage.service;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto.AiChatMessageDto;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.dto.AiExchangeDto;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.entity.AiChatMessage;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.MessageStatus;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.enums.SenderType;
import com.moci_3d_backend.domain.chat.ai.aiChatMessage.repository.AiChatMessageRepository;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.entity.AiChatRoom;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import com.moci_3d_backend.domain.user.entity.User;
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
    private final AiChatMessageRateLimitService aiChatMessageRateLimitService;


    public AiChatMessage create(User actor, Long roomId, SenderType senderType, String content) {

        AiChatRoom room = aiChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));


        AiChatMessage message = AiChatMessage.builder()
                .sender(actor)
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
    public AiExchangeDto ask(User actor, Long roomId, String content) {

        // 추정 토큰 수 아주 보수적,간단 추정
        long tokensNeeded = Math.max(1, content.length());

        aiChatMessageRateLimitService.checkRateLimitsOrThrow(1L, tokensNeeded); // TODO: userId 나중에 넣기

        // 사용자 메시지 저장
        AiChatMessage userMessage = create(actor, roomId, SenderType.HUMAN, content);

        // 히스토리 프롬프트 구성
        List<AiChatMessage> history = aiChatMessageRepository.findByRoomIdOrderByIdAsc(roomId);
        String prompt = buildPrompt(history);

        // AI 호출
        String aiText = geminiClient.generateChatResponse(prompt);

        // Ai 메시지 저장
        AiChatMessage aiMessage = create(actor, roomId, SenderType.AI, aiText);

        return new AiExchangeDto(
                new AiChatMessageDto(userMessage),
                new AiChatMessageDto(aiMessage)
        );
    }


    private String buildPrompt(List<AiChatMessage> history) {
        //직전 대화 N개로 프롬프트 구성

        final int DEFAULT_CONTEXT_SIZE = 10;

        int N = Math.max(0, history.size() - DEFAULT_CONTEXT_SIZE);
        List<AiChatMessage> last = history.subList(N, history.size());

        String hist = last.stream()
                .map(m -> (m.getSenderType() == SenderType.HUMAN ? "Human: " : "AI: ") + m.getContent())
                .collect(Collectors.joining("\n"));

        String system = """
                너는 디지털 소외계층을 위한 50대 이상 사용자를 돕는 한국인 AI 도우미야.  
                50대 이상이라고 하지말아줘.(나이 언급하지 말아줘)
                사용자가 컴퓨터, 스마트폰, 인터넷을 쉽게 사용할 수 있도록 친절하고 이해하기 쉽게 설명해줘.
                사용자가 질문하면, 최대한 쉽게 단계별로 설명해줘.
                
                답변은 반드시 아래 형식을 따라야 해:
                
                - 설명의 목표를 1~2줄로 간단히 작성    
                - 각 단계로 핵심만 안내 (각 단계는 2줄 이내)  
                - 전체 300자 이내  
                - 어려운 용어 없이 쉬운 표현 사용  
                - 버튼·메뉴명은 그대로 제시
                - 짧고 간략하게 작성좀
                """;

        return system + "\n\n" + hist + "\n\nAI:";
    }


    @Transactional(readOnly = true)
    public List<AiChatMessage> listMessages(User actor, Long roomId, Long fromId) {
        AiChatRoom room = aiChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));

        if (!actor.getRole().equals(User.UserRole.ADMIN) &&
                !room.getUser().getId().equals(actor.getId())) {
            throw new ServiceException(403, "해당 채팅방 메시지에 접근할 권한이 없습니다.");
        }


        if (fromId == null) {
            return aiChatMessageRepository.findByRoomIdOrderByIdAsc(roomId);
        }

        //  fromId 이후 메시지만 가져오기
        return aiChatMessageRepository.findByRoomIdAndIdGreaterThanOrderByIdAsc(roomId, fromId);
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
                .orElseThrow(() -> new ServiceException(404, "lastSeenMessageId가 존재하지 않습니다."));
        if (!last.getRoom().getId().equals(roomId)) {
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


    @Transactional(readOnly = true)
    public List<AiChatMessage> searchAll(User actor, Long roomId, String query) {
        AiChatRoom room = aiChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다,"));

        if (!actor.getRole().equals(User.UserRole.ADMIN) &&
                !room.getUser().getId().equals(actor.getId())) {
            throw new ServiceException(403, "해당 채팅방 메시지에 접근할 권한이 없습니다.");
        }

        String likeKeyword = "%" + query + "%";

        return aiChatMessageRepository.findByRoomIdAndContentLikeOrderByIdAsc(roomId, likeKeyword);

    }

    @Transactional
    public void delete(User actor, Long roomId, Long messageId) {
        AiChatRoom room = aiChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 AI 채팅방입니다."));

        AiChatMessage message = aiChatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ServiceException(404, "존재하지 않는 메시지입니다."));

        if (!actor.getRole().equals(User.UserRole.ADMIN) &&
                !room.getUser().getId().equals(actor.getId())) {
            throw new ServiceException(403, "해당 채팅방 메시지에 접근할 권한이 없습니다.");
        }

        // 메시지가 해당 채팅방에 속하는지 검증
        if (!message.getRoom().getId().equals(room.getId())) {
            throw new ServiceException(400, "해당 메시지는 요청한 roomId에 속하지 않습니다.");
        }


        aiChatMessageRepository.delete(message);
    }
}
