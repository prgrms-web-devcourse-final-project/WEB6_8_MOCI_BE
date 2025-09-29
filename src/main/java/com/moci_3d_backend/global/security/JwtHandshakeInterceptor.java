package com.moci_3d_backend.global.security;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {

        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest)request;
        HttpServletRequest servletRequest = servletServerHttpRequest.getServletRequest();
        Optional<String> token = Arrays.stream(servletRequest.getCookies())
                .filter((e)->e.getName().equals("accessToken"))
                .map(e->e.getValue())
                .findFirst();
        if (token.isEmpty()) {
            return false;
        }
        attributes.put("token", token.get());
        Map<String, Object> payload = userService.payload(token.get());
        if (payload == null) {
            return false;
        }
        int id = (int) payload.get("id");
        String userId = (String) payload.get("userId");
        String name = (String) payload.get("name");
        String role = (String) payload.get("role");
        UserDetails userDetails = new SecurityUser(
                id,
                userId,
                "",
                name,
                List.of(new SimpleGrantedAuthority(role))
        );
        attributes.put("user", userDetails);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
