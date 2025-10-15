package com.moci_3d_backend.global.security;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.rq.Rq;
import jakarta.transaction.Transactional;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final Rq rq;
    
    @Value("${custom.frontend.baseUrl}")
    private String frontendBaseUrl;
    
    @Value("${custom.frontend.registerPath}")
    private String registerPath;
    
    @Value("${custom.frontend.mainPath}")
    private String mainPath;
    
    @Value("${custom.accessToken.expirationSeconds}")
    private int accessTokenExpirationSeconds;
    
    @Value("${custom.refreshToken.expirationSeconds}")
    private int refreshTokenExpirationSeconds;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인 성공: {}", authentication.getName());
        
        // 🔍 설정 값 디버깅
        log.debug("=== 설정 값 확인 ===");
        log.debug("frontendBaseUrl: {}", frontendBaseUrl);
        log.debug("registerPath: {}", registerPath);
        log.debug("mainPath: {}", mainPath);
        
        // SecurityUser에서 사용자 정보 추출
        User actor = null;
        if (authentication.getPrincipal() instanceof SecurityUser securityUser) {
            // UserService에서 새로 조회 (세션 문제 해결)
            actor = userService.findByUserId(securityUser.getUsername());
            
            // JWT 토큰 생성 (UserService의 genAccessToken 사용)
            String accessToken = userService.genAccessToken(actor);

            // 쿠키 설정
            rq.setCookie("accessToken", accessToken, accessTokenExpirationSeconds);
            rq.setCookie("refreshToken", actor.getRefreshToken(), refreshTokenExpirationSeconds);
            
            log.info("OAuth2 로그인 완료 - 사용자: {}", actor.getUserId());
        } else {
            log.error("OAuth2 로그인 실패: SecurityUser를 찾을 수 없음");
            return;
        }

        // ✅ state 파라미터 확인 (우선순위: state > digital_level)
        String stateParam = request.getParameter("state");
        String redirectUrl = null;

        if (stateParam != null) {
            try {
                // 1️⃣ Base64 URL-safe 디코딩
                String decodedStateParam = new String(Base64.getUrlDecoder().decode(stateParam), StandardCharsets.UTF_8);

                // 2️⃣ '#' 앞은 redirectUrl, 뒤는 originState
                redirectUrl = decodedStateParam.split("#", 2)[0];
                log.info("state 파라미터에서 리다이렉트 URL 추출: {}", redirectUrl);
            } catch (Exception e) {
                log.warn("state 파라미터 디코딩 실패: {}", e.getMessage());
            }
        }

        // ✅ state가 없거나 실패한 경우 digital_level 기반 리다이렉트 URL 결정
        if (redirectUrl == null || redirectUrl.isEmpty() || redirectUrl.equals("/")) {
            redirectUrl = determineRedirectUrl(actor);
            log.info("digital_level 기반 리다이렉트 URL 사용: {}", redirectUrl);
        }

        // ✅ 최종 리다이렉트
        response.sendRedirect(redirectUrl);
        
        log.info("OAuth2 로그인 완료 - 사용자: {}, digital_level: {}, 리다이렉트: {}", 
                actor.getUserId(), actor.getDigitalLevel(), redirectUrl);
    }
    
    private String determineRedirectUrl(User actor) {
        try {
            if (actor.getDigitalLevel() == null) {
                // 신규 가입자: 회원가입 페이지로 리다이렉트
                String redirectUrl = frontendBaseUrl + registerPath;
                log.info("신규 가입자 감지 - 리다이렉트: {}", redirectUrl);
                return redirectUrl;
            } else {
                // 기존 가입자: 메인 페이지로 리다이렉트
                String redirectUrl = frontendBaseUrl + mainPath;
                log.info("기존 가입자 감지 (digital_level: {}) - 리다이렉트: {}", 
                        actor.getDigitalLevel(), redirectUrl);
                return redirectUrl;
            }
        } catch (Exception e) {
            log.error("리다이렉트 URL 생성 중 오류 발생: {}", e.getMessage());
            // 기본값으로 fallback
            return frontendBaseUrl;
        }
    }
}
