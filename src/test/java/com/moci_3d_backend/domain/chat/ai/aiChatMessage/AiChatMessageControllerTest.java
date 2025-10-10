package com.moci_3d_backend.domain.chat.ai.aiChatMessage;

import com.moci_3d_backend.domain.user.service.UserService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AiChatMessageControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mvc;

    //비회원 일때
    @Test
    @DisplayName("AI 1번 채팅방 메세지 목록 조회(비회원)")
    void t1() throws Exception {
        Long rooId = 1L;

        ResultActions result = mvc.perform(post("/api/v1/chat/ai/rooms/%d/messages".formatted(rooId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."));
    }

    //회원 일때
    @Test
    @DisplayName("AI 1번 채팅방 메세지 목록 조회(회원)")
    void t2() throws Exception {
        Long rooId = 1L;

        ResultActions result = mvc.perform(post("/api/v1/chat/ai/rooms/%d/messages".formatted(rooId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."));
    }


}
