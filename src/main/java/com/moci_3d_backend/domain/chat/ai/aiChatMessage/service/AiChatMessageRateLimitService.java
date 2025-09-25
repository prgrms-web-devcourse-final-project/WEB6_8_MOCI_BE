package com.moci_3d_backend.domain.chat.ai.aiChatMessage.service;

import com.moci_3d_backend.domain.chat.ai.aiChatMessage.repository.AiChatMessageRepository;
import com.moci_3d_backend.domain.chat.ai.aiChatRoom.repository.AiChatRoomRepository;
import com.moci_3d_backend.external.ai.client.GeminiClient;
import com.moci_3d_backend.global.exception.ServiceException;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AiChatMessageRateLimitService {

    private final AiChatRoomRepository aiChatRoomRepository;
    private final AiChatMessageRepository aiChatMessageRepository;
    private final GeminiClient geminiClient;

    // rate limit 키-버켓 저장
    private final ConcurrentHashMap<String, Bucket> reqBuckets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Bucket> tokenBuckets = new ConcurrentHashMap<>();

    // rpm tpm
    private Bucket getRequestBucket(String key) {
        return reqBuckets.computeIfAbsent(key, k ->
                Bucket.builder()
                        .addLimit(l -> l.capacity(30)
                                .refillGreedy(30, java.time.Duration.ofMinutes(1))) // RPM 30
                        .addLimit(l -> l.capacity(400)
                                .refillIntervally(400, java.time.Duration.ofDays(1))) // RPD 400
                        .build()
        );
    }

    // tpm
    private Bucket getTokenBucket(String key) {
        return tokenBuckets.computeIfAbsent(key, k ->
                Bucket.builder()
                        .addLimit(limit -> limit.capacity(1_000_000)
                                .refillGreedy(1_000_000, Duration.ofMinutes(1)))
                        .build()
        );
    }

    private static String rateKey(Long userId, Long roomId) {
        if (userId != null && roomId != null) return "u:" + userId + "|r:" + roomId;
        if (userId != null) return "u:" + userId;
        if (roomId != null) return "r:" + roomId;
        return "global";
    }

    private void checkRateLimitsOrThrow(Long userId, Long roomId, long tokensNeeded) {
        String key = rateKey(userId, roomId);

        // 요청 카운트 제한
        Bucket reqBucket = getRequestBucket(key);
        if (!reqBucket.tryConsume(1)) {
            throw new ServiceException(429, "요청 한도를 초과했습니다. 잠시 후 다시 시도해주세요.");
        }

        // 토큰 제한
        Bucket tokBucket = getTokenBucket(key);
        if (!tokBucket.tryConsume(tokensNeeded)) {
            throw new ServiceException(429, "토큰 한도를 초과했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

}
