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

    // 전역 버킷 ( 모든 사용자와 방에 적용)
    private final Bucket globalRequestBucket = Bucket.builder()
            .addLimit(l -> l.capacity(15) // RPM 15
                    .refillGreedy(15, Duration.ofMinutes(1)))
            .addLimit(l -> l.capacity(200) // RPD 200
                    .refillIntervally(200, Duration.ofDays(1)))
            .build();

    private final Bucket globalTokenBucket = Bucket.builder()
            .addLimit(l -> l.capacity(1_000_000) // TPM 1,000,000
                    .refillGreedy(1_000_000, Duration.ofMinutes(1)))
            .build();

    // 사용자 및 방별 버킷
    private final ConcurrentHashMap<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    private Bucket getUserBucket(Long userId) {
        return userBuckets.computeIfAbsent("u:" + userId, k ->
                Bucket.builder()
                        .addLimit(l -> l.capacity(10) // 유저당 RPM 10
                                .refillGreedy(10, Duration.ofMinutes(1)))
                        .addLimit(l -> l.capacity(200) // 유저당 RPD 200
                                .refillIntervally(200, Duration.ofDays(1)))
                        .build()
        );
    }

    public void checkRateLimitsOrThrow(Long userId, long tokensNeeded) {
       // 전역 요청 제한
        if (!globalRequestBucket.tryConsume(1)) {
            throw new ServiceException(429, "전역 요청 한도를 초과했습니다. 잠시 후 다시 시도해주세요.");
        }

        // 전역 토큰 제한
        if (!globalTokenBucket.tryConsume(tokensNeeded)) {
            throw new ServiceException(429, "전역 토큰 한도를 초과했습니다. 잠시 후 다시 시도해주세요.");
        }

        // 사용자 보조 제한
        if (userId != null) {
            Bucket userBucket = getUserBucket(userId);
            if (!userBucket.tryConsume(1)) {
                throw new ServiceException(429, "해당 사용자의 요청 한도를 초과했습니다.");
            }
        }
    }

}
