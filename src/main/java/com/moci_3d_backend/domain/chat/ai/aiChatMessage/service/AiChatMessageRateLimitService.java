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

    // 전역
    private static final int GLOBAL_RPM = 15;
    private static final int GLOBAL_RPD = 200;
    private static final long GLOBAL_TPM = 1_000_000;
    // 사용자
    private static final int USER_RPM = 10;
    private static final int USER_RPD = 200;


    // 전역 버킷 ( 모든 사용자와 방에 적용)
    private final Bucket globalRequestBucket = Bucket.builder()
            .addLimit(l -> l.capacity(GLOBAL_RPM) // RPM 15
                    .refillGreedy(GLOBAL_RPM, Duration.ofMinutes(1)))
            .addLimit(l -> l.capacity(GLOBAL_RPD) // RPD 200
                    .refillIntervally(GLOBAL_RPD, Duration.ofDays(1)))
            .build();

    private final Bucket globalTokenBucket = Bucket.builder()
            .addLimit(l -> l.capacity(GLOBAL_TPM) // TPM 1,000,000
                    .refillGreedy(GLOBAL_TPM, Duration.ofMinutes(1)))
            .build();

    // 사용자 및 방별 버킷
    private final ConcurrentHashMap<String, Bucket> userBuckets = new ConcurrentHashMap<>();

    private Bucket getUserBucket(Long userId) {
        return userBuckets.computeIfAbsent("u:" + userId, k ->
                Bucket.builder()
                        .addLimit(l -> l.capacity(USER_RPM) // 유저당 RPM 10
                                .refillGreedy(USER_RPM, Duration.ofMinutes(1)))
                        .addLimit(l -> l.capacity(USER_RPD) // 유저당 RPD 200
                                .refillIntervally(USER_RPD, Duration.ofDays(1)))
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
