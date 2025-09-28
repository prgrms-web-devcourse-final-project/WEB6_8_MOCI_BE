package com.moci_3d_backend.global.security;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.exception.ServiceException;
import com.moci_3d_backend.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final Rq rq;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            work(request, response, filterChain);
        } catch (ServiceException e) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write(
                    """
                            {
                                "code": "%s",
                                "msg": "%s"
                            }
                            """.formatted(e.getCode(), e.getCode())
            );
        }
    }

    private void work(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // API 요청이 아니라면 패스
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 인증, 인가가 필요없는 API 요청이라면 패스
        if (List.of("/api/v1/auth/login", "/api/v1/auth/signup", "/api/v1/members/join").contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken;

        String headerAuthorization = rq.getHeader("Authorization", "");

        if (!headerAuthorization.isBlank()) {
            if (!headerAuthorization.startsWith("Bearer "))
                throw new ServiceException(401, "Authorization 헤더가 Bearer 형식이 아닙니다.");

            String[] headerAuthorizationBits = headerAuthorization.split(" ", 2);

            refreshToken = headerAuthorizationBits[1];
        } else {
            refreshToken = rq.getCookieValue("refreshToken", "");
        }

        boolean isRefreshTokenExists = !refreshToken.isBlank();

        if (!isRefreshTokenExists) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = userService
                .findByRefreshKey(refreshToken)
                .orElseThrow(() -> new ServiceException(403, "API 키가 유효하지 않습니다."));

        UserDetails securityUser = new SecurityUser(
                user.getId(),
                user.getUserId(),
                "",
                user.getName(),
                user.getAuthorities()
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                securityUser.getPassword(),
                securityUser.getAuthorities()
        );

        // 이 시점 이후부터는 시큐리티가 이 요청을 인증된 사용자의 요청으로 인식
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}