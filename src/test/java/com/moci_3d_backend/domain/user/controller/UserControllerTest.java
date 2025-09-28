package com.moci_3d_backend.domain.user.controller;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import org.apache.hc.core5.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("내 정보 without Token - 실패")
    void t1() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/users/me")

                )
                .andDo(print());

        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."));
    }

    @Test
    @DisplayName("내 정보 with Authorization(refreshToken, wrong accessToken) - 성공(엑세스 토큰은 자동 재발급)")
    void t2() throws Exception {
        User user = userService.findByUserId("01012345678");

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/users/me")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + user.getRefreshToken() + " wrong-access-token")
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("getMe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.data.socialId").value(user.getSocialId()))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.role").value(user.getRole().name()))
                .andExpect(jsonPath("$.data.digitalLevel").value(user.getDigitalLevel()))
                .andExpect(jsonPath("$.data.createdAt").value(Matchers.startsWith(user.getCreatedAt().toString().substring(0, 20))));

        resultActions.andExpect(
                result -> {
                    Cookie accessTokenCookie = result.getResponse().getCookie("accessToken");
                    assertThat(accessTokenCookie.getValue()).isNotEmpty();
                    assertThat(accessTokenCookie.getPath()).isEqualTo("/");
                    assertThat(accessTokenCookie.getAttribute("HttpOnly")).isEqualTo("true");

                    String authorizationHeader = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
                    assertThat(authorizationHeader).isNotEmpty();
                }
        );
    }

    @Test
    @DisplayName("내 정보 with Authorization(accessToken) - 성공")
    void t3() throws Exception {
        User user = userService.findByUserId("01012345678");

        String actorAccessToken = userService.genAccessToken(user);

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/users/me")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer wrong-refresh-token " + actorAccessToken)
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("getMe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.data.socialId").value(user.getSocialId()))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.role").value(user.getRole().name()))
                .andExpect(jsonPath("$.data.digitalLevel").value(user.getDigitalLevel()))
                .andExpect(jsonPath("$.data.createdAt").value(Matchers.startsWith(user.getCreatedAt().toString().substring(0, 20))));
    }

    @Test
    @DisplayName("내 정보 with Cookie - 성공")
    void t4() throws Exception {
        User user = userService.findByUserId("01012345678");

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/users/me")
                                .cookie(new Cookie("refreshToken", user.getRefreshToken()))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("getMe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.data.socialId").value(user.getSocialId()))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.role").value(user.getRole().name()))
                .andExpect(jsonPath("$.data.digitalLevel").value(user.getDigitalLevel()))
                .andExpect(jsonPath("$.data.createdAt").value(Matchers.startsWith(user.getCreatedAt().toString().substring(0, 20))));
    }
}
