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

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인 성공: {}", authentication.getName());
        
        // SecurityUser에서 사용자 정보 추출
        User actor = null;
        if (authentication.getPrincipal() instanceof SecurityUser securityUser) {
            // UserService에서 새로 조회 (세션 문제 해결)
            actor = userService.findByUserId(securityUser.getUsername());
            
            // JWT 토큰 생성 (UserService의 genAccessToken 사용)
            String accessToken = userService.genAccessToken(actor);

            // 쿠키 설정
            rq.setCookie("accessToken", accessToken);
            rq.setCookie("refreshToken", actor.getRefreshToken());
            
            log.info("OAuth2 로그인 완료 - 사용자: {}", actor.getUserId());
        } else {
            log.error("OAuth2 로그인 실패: SecurityUser를 찾을 수 없음");
            return;
        }

        // ✅ 기본 리다이렉트 URL
        String redirectUrl = "/";

        // ✅ state 파라미터 확인
        String stateParam = request.getParameter("state");

        if (stateParam != null) {
            // 1️⃣ Base64 URL-safe 디코딩
            String decodedStateParam = new String(Base64.getUrlDecoder().decode(stateParam), StandardCharsets.UTF_8);

            // 2️⃣ '#' 앞은 redirectUrl, 뒤는 originState
            redirectUrl = decodedStateParam.split("#", 2)[0];
        }

        // ✅ 최종 리다이렉트
        response.sendRedirect(redirectUrl);
        
        log.info("OAuth2 로그인 완료 - 사용자: {}, 리다이렉트: {}", actor.getUserId(), redirectUrl);
    }
}
