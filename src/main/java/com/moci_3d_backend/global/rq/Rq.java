package com.moci_3d_backend.global.rq;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.security.SecurityUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final UserService userService;
    
    @org.springframework.beans.factory.annotation.Value("${custom.site.cookieDomain:localhost}")
    private String cookieDomain;

    public User getActor() {
        return Optional.ofNullable(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                )
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof SecurityUser)
                .map(principal -> (SecurityUser) principal)
                .map(securityUser -> userService.getReferenceById(securityUser.getId()))
                .orElse(null);
    }

    // 쿠키 값 가져오기 (없으면 기본값 반환)
    public String getCookieValue(String name, String defaultValue) {
        if (req.getCookies() == null) {
            return defaultValue;
        }

        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals(name) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                return cookie.getValue();
            }
        }
        return defaultValue;
    }

    // 쿠키 설정하기
    public void setCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value != null ? value : "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        // Domain 제거 → 현재 요청 도메인 기준 자동 설정됨
        // cookie.setDomain("localhost"); ❌

        // HTTP 환경에서는 Secure=false
        cookie.setSecure(false);

        // 1년 유지
        cookie.setMaxAge((value == null || value.isBlank()) ? 0 : 60 * 60 * 24 * 365);

        // SameSite=None 으로 설정 (Servlet 6 이상)
        cookie.setAttribute("SameSite", "None");

        resp.addCookie(cookie);
    }


    // 쿠키 삭제 (값 null로 설정)
    public void deleteCookie(String name) {
        setCookie(name, null);
    }

    public String getHeader(String name, String defaultValue) {
        String value = req.getHeader(name);
        return value != null ? value : defaultValue;
    }

    public void setHeader(String name, String value) {
        resp.setHeader(name, value);
    }
}