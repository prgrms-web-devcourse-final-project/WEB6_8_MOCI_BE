package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("로그인(토큰 발급) - 성공")
    void t2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "loginType": "PHONE",
                                            "userId": "01012345678",
                                            "password": "admin123"
                                        }
                                        """.stripIndent())
                )
                .andDo(print());

        User user = userService.findByUserId("01012345678");

        resultActions
                .andExpect(handler().handlerType(AuthController.class))
                .andExpect(handler().methodName("token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.user").exists())
                .andExpect(jsonPath("$.data.user.id").value(user.getId()))
                .andExpect(jsonPath("$.data.user.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.data.user.name").value(user.getName()))
                .andExpect(jsonPath("$.data.user.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.user.role").value(user.getRole().name()))
                .andExpect(jsonPath("$.data.user.digitalLevel").value(user.getDigitalLevel()))
                .andExpect(jsonPath("$.data.user.createdAt").value(Matchers.startsWith(user.getCreatedAt().toString().substring(0, 20))));

        resultActions.andExpect(
                result -> {
                    Cookie accessTokenCookie = result.getResponse().getCookie("accessToken");
                    assertThat(accessTokenCookie.getValue()).isNotEmpty();
                    assertThat(accessTokenCookie.getPath()).isEqualTo("/");
                    assertThat(accessTokenCookie.getAttribute("HttpOnly")).isEqualTo("true");

                    Cookie refreshTokenCookie = result.getResponse().getCookie("refreshToken");
                    assertThat(refreshTokenCookie.getValue()).isEqualTo(user.getRefreshToken());
                    assertThat(refreshTokenCookie.getPath()).isEqualTo("/");
                    assertThat(refreshTokenCookie.getAttribute("HttpOnly")).isEqualTo("true");
                }
        );
    }

    @Test
    @DisplayName("로그아웃")
    void t3() throws Exception {
        // 1단계: 로그인해서 토큰 발급
        ResultActions loginResult = mvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "loginType": "PHONE",
                                            "userId": "01012345678",
                                            "password": "admin123"
                                        }
                                        """.stripIndent())
                );

        // 2단계: 로그인후 쿠키 추출
        Cookie accessTokenCookie = loginResult.andReturn().getResponse().getCookie("accessToken");
        Cookie refreshTokenCookie = loginResult.andReturn().getResponse().getCookie("refreshToken");

        // 3단계: 로그아웃 요청
        ResultActions resultActions = mvc
                .perform(
                        delete("/api/v1/auth/logout")
                                .cookie(accessTokenCookie)
                                .cookie(refreshTokenCookie)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(AuthController.class))
                .andExpect(handler().methodName("deleteToken"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Cookie refreshTokenCookieResponse = result.getResponse().getCookie("refreshToken");
                    assertThat(refreshTokenCookieResponse.getValue()).isEmpty();
                    assertThat(refreshTokenCookieResponse.getMaxAge()).isEqualTo(0);
                    assertThat(refreshTokenCookieResponse.getPath()).isEqualTo("/");
                    assertThat(refreshTokenCookieResponse.isHttpOnly()).isTrue();

                    Cookie accessTokenCookieResponse = result.getResponse().getCookie("accessToken");
                    assertThat(accessTokenCookieResponse.getValue()).isEmpty();
                    assertThat(accessTokenCookieResponse.getMaxAge()).isEqualTo(0);
                    assertThat(accessTokenCookieResponse.getPath()).isEqualTo("/");
                    assertThat(accessTokenCookieResponse.isHttpOnly()).isTrue();
                });
    }
}
