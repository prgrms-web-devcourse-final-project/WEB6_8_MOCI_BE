package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApiV1ChatMessageRestControllerTest {
    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("채팅 내역 조회")
    void t1() throws Exception {
        User user = userService.findByUserId("01045678901");
        String refreshToken = user.getRefreshToken();
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/chat/mentor/message/%d".formatted(1L))
                                .cookie(new Cookie("refreshToken", refreshToken))
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1ChatMessageRestController.class))
                .andExpect(handler().methodName("getMessages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success to get messages"))
                ;
    }
}
