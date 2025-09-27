package com.moci_3d_backend.global.rq;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

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
        cookie.setDomain("localhost");
        cookie.setSecure(true);
        cookie.setMaxAge((value == null || value.isBlank()) ? 0 : 60 * 60 * 24 * 365);

        // SameSite 설정 (Servlet Cookie API에는 직접 없으므로 response header 조작 필요)
        // Servlet 6.0 이상에서는 cookie.setAttribute("SameSite", "Strict") 가능
        cookie.setAttribute("SameSite", "Strict");

        resp.addCookie(cookie);
    }

    // 쿠키 삭제 (값 null로 설정)
    public void deleteCookie(String name) {
        setCookie(name, null);
    }
}