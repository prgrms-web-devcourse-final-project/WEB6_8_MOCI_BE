package com.moci_3d_backend.global.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Websocket
 */
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        // TODO accessToken을 사용한 로그인이 구현되면 주석 해제
//        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest)request;
//        HttpServletRequest servletRequest = servletServerHttpRequest.getServletRequest();
//        Optional<String> token = Arrays.stream(servletRequest.getCookies())
//                .filter((e)->e.getName().equals("accessToken"))
//                .map(e->e.getValue())
//                .findFirst();
//        if (token.isEmpty()) {
//            return false;
//        }
//        attributes.put("token", token.get());

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
