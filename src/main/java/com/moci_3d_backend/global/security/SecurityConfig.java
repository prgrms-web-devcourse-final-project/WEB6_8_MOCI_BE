package com.moci_3d_backend.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
    private final CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;

    /*
    * RoleHierarchy 설정
    * ADMIN > MENTOR > USER
    * ADMIN 권한은 MENTOR, USER 권한을 포함
    * MENTOR 권한은 USER 권한을 포함
    */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(
                "ROLE_ADMIN > ROLE_MENTOR\n" +
                "ROLE_MENTOR > ROLE_USER"
        );
        return roleHierarchy;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 모든 요청 허용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/*",
                                "/_next/**",
                                "/favicon.ico",
                                "/h2-console/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**", // OPEN API 문서의 JSON 버전
                                "/webjars/**",
                                "/actuator/**", // 헬스체크, 무중단배포에 필요
                                "/api/v1/file/**",
                                "/chat/room/**",
                                "/sse/**",
                                "/uploads/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/*/archive/public/**")
                        .permitAll()
                        .requestMatchers("/api/*/auth/login", "/api/*/auth/register", "/api/*/auth/token")
                        .permitAll()
                        .anyRequest().authenticated()
                )

                // CSRF 비활성화 (H2 콘솔 사용 위해 필요)
                .csrf(AbstractHttpConfigurer::disable)

                // H2 콘솔 사용 허용
                .headers(headers ->
                        headers.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                )

                // 기본 인증 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(customOAuth2LoginSuccessHandler)
                        .authorizationEndpoint(
                                authorizationEndpoint -> authorizationEndpoint
                                        .authorizationRequestResolver(customOAuth2AuthorizationRequestResolver)
                        )
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setContentType("application/json;charset=UTF-8");

                                            response.setStatus(401);
                                            response.getWriter().write(
                                                    """
                                                            {
                                                                "code": "401",
                                                                "msg": "로그인이 필요합니다."
                                                            }
                                                            """.stripIndent().trim()
                                            );
                                        }
                                )
                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) -> {
                                            response.setContentType("application/json;charset=UTF-8");

                                            response.setStatus(403);
                                            response.getWriter().write(
                                                    """
                                                            {
                                                                "code": "403",
                                                                "msg": "권한이 없습니다."
                                                            }
                                                            """.stripIndent().trim()
                                            );
                                        }
                                )
                );

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 허용할 오리진 설정
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",           // 로컬 프론트엔드
//            "http://15.165.137.2:8080",       // EC2 IP (테스트용)
//            "https://www.moci.oa.gg",         // 프로덕션 프론트엔드 (나중에)
            "https://api.mydidimdol.com"          // 프로덕션 백엔드 (cloudflare 프록싱)
        ));
        // 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // 자격 증명 허용 설정
        configuration.setAllowCredentials(true);
        // 허용할 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 노출할 헤더 설정 (프론트엔드에서 읽을 수 있는 헤더)
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
        // CORS 설정을 소스에 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);  // 모든 경로에 CORS 적용 -> 위의 도메인만 허용
        return source;
    }
}