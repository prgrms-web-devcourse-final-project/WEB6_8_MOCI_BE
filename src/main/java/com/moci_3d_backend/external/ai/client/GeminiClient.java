package com.moci_3d_backend.external.ai.client;

import com.moci_3d_backend.external.ai.dto.GeminiRequest;
import com.moci_3d_backend.external.ai.dto.GeminiResponse;
import com.moci_3d_backend.global.exception.GeminiApiException;
import com.moci_3d_backend.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    // 동시성 제한 설정 (최대 10개)
    private final Semaphore permits = new Semaphore(10,true);
    private static final long TIMEOUT_MS = 300;

    public String generateChatResponse(String prompt) {
        boolean acquired = false;

        try {
            acquired = permits.tryAcquire(TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!acquired) {
                throw new ServiceException(503,"AI가 혼잡합니다. 잠시 후 다시 시도해주세요.");
            }

            GeminiRequest request = GeminiRequest.of(prompt);

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String urlWithKey = buildApiUrl();

            HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers); // 요청 데이터 + 헤더

            log.info("Gemini API 호출: {}", prompt);

            // API 호출
            ResponseEntity<GeminiResponse> response = restTemplate.postForEntity(
                    urlWithKey,
                    entity,
                    GeminiResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String generatedText = response.getBody().getGeneratedText();
                if (generatedText == null || generatedText.trim().isEmpty()) {
                    throw new GeminiApiException("Gemini API에서 빈 응답을 받았습니다.");
                }
                return generatedText;
            } else {
                throw new GeminiApiException("Gemini API 호출 실패: " + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("Gemini API 호출 중 오류 발생", e);
            throw new GeminiApiException("Gemini API 연결 실패: " + e.getMessage());
        } finally {
            if (acquired) {
                permits.release();
            }
        }
    }

    private String buildApiUrl() {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("key", apiKey)
                .toUriString();
    }

}
