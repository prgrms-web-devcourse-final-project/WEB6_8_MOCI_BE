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
                            """.formatted(e.getCode(), e.getMsg())
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
        if (List.of(
                "/api/v1/auth/login",
                "/api/v1/auth/signup",
                "/api/v1/auth/register",
                "/api/v1/auth/token",
                "/api/v1/members/join",
                "/api/v1/users/phone-check"  // 전화번호 중복확인
        ).contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken;
        String accessToken;

        String headerAuthorization = rq.getHeader("Authorization", "");

        if (!headerAuthorization.isBlank()) {
            if (!headerAuthorization.startsWith("Bearer "))
                throw new ServiceException(401, "Authorization 헤더가 Bearer 형식이 아닙니다.");

            String[] headerAuthorizationBits = headerAuthorization.split(" ", 3);

            refreshToken = headerAuthorizationBits[1];
            accessToken = headerAuthorizationBits.length == 3 ? headerAuthorizationBits[2] : "";
        } else {
            refreshToken = rq.getCookieValue("refreshToken", "");
            accessToken = rq.getCookieValue("accessToken", "");
        }

        boolean isRefreshTokenExists = !refreshToken.isBlank();
        boolean isAccessTokenExists = !accessToken.isBlank();

        if (!isRefreshTokenExists && !isAccessTokenExists) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = null;
        boolean isAccessTokenValid = false;

        if (isAccessTokenExists) {
            Map<String, Object> payload = userService.payload(accessToken);

            if (payload != null) {
                int id = (int) payload.get("id");
                String userId = (String) payload.get("userId");
                String name = (String) payload.get("name");
                String role = (String) payload.get("role");
                user = new User();
                user.setId((long) id);
                user.setUserId(userId);
                user.setName(name);
                user.setRole(User.UserRole.valueOf(role));

                isAccessTokenValid = true;
            }
        }

        if (user == null) {
            user = userService
                    .findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new ServiceException(401, "API 키가 유효하지 않습니다."));
        }

        if (isAccessTokenExists && !isAccessTokenValid) {
            String actorAccessToken = userService.genAccessToken(user);

            rq.setCookie("accessToken", actorAccessToken);
            rq.setHeader("Authorization", "Bearer " + refreshToken + " " + actorAccessToken);
        }

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