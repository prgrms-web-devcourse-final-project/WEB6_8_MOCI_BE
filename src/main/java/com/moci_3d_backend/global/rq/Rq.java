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
        
        // 프로덕션에서는 점(.)으로 시작하는 도메인으로 설정하여 서브도메인 간 공유
        // 예: .mydidimdol.com (www, api 모두에서 사용 가능)
        if (!"localhost".equals(cookieDomain)) {
            cookie.setDomain("." + cookieDomain);  // .mydidimdol.com
        } else {
            cookie.setDomain(cookieDomain);  // localhost
        }
        
        // HTTP 환경에서는 Secure를 false로 설정
        // TODO: HTTPS 적용 후 true로 변경 필요
        cookie.setSecure(false);
        
        cookie.setMaxAge((value == null || value.isBlank()) ? 0 : 60 * 60 * 24 * 365);

        // HTTP 환경에서는 SameSite=Lax 사용
        // HTTPS 적용 후에는 SameSite=None으로 변경 권장
        cookie.setAttribute("SameSite", "Lax");

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