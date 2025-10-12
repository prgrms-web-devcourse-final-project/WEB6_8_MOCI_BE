package com.moci_3d_backend.domain.chat.ai.aiChatMessage;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Disabled;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("AI 1번 채팅방 메세지 목록 조회(1번 방을 가진 회원)")
    void t2() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        Long rooId = 1L;

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/rooms/%d/messages".formatted(rooId))
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result
                .andExpect(status().isOk())
                // 첫 번쨰 메시지 검증
                .andExpect(jsonPath("$[0].senderType").value("HUMAN"))
                .andExpect(jsonPath("$[0].content").value("안녕!"))
                // 두 번째 메시지 검증
                .andExpect(jsonPath("$[1].senderType").value("AI"))
                .andExpect(jsonPath("$[1].content").value("네, 반갑습니다! 무엇을 도와드릴까요?"));
    }

    @Test
    @DisplayName("AI 2번 채팅방 메세지 목록 조회(1번 방을 가진 회원) -> 권한 없음")
    void t3() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        Long rooId = 3L;

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/rooms/%d/messages".formatted(rooId))
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("해당 채팅방 메시지에 접근할 권한이 없습니다."));
    }

    @Test
    @DisplayName("AI 1번 채팅방 메세지 생성, AI 응답 동기 (1번 방을 가진 회원)")
    @Disabled("실제 AI 호출 테스트 - 필요할 때만 실행")
    void t4() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        Long rooId = 1L;

        String str = """
                {
                    "content": "안녕"
                }
                """;

        ResultActions result = mvc.perform(post("/api/v1/chat/ai/rooms/%d/ask".formatted(rooId))
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("AI 응답을 받았습니다."))
                // userMessage 검증
                .andExpect(jsonPath("$.data.userMessage.senderType").value("HUMAN"))
                .andExpect(jsonPath("$.data.userMessage.content").value("안녕"))
                // aiMessage 검증
                .andExpect(jsonPath("$.data.aiMessage.senderType").value("AI"))
                .andExpect(jsonPath("$.data.aiMessage.content").exists());

    }

    @Test
    @DisplayName("AI 1번 채팅방 원하는 메세지 검색 조회(1번 방을 가진 회원)")
    void t5() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        Long rooId = 1L;

        String keyword = "안녕";

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/rooms/%d/messages/search?query=%s".formatted(rooId, keyword))
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("1개의 메시지를 찾았습니다."))
                // data 배열 크기 확인
                .andExpect(jsonPath("$.data", hasSize(1)))
                // 첫 번째 메시지 검증
                .andExpect(jsonPath("$.data[0].roomId").value(1))
                .andExpect(jsonPath("$.data[0].senderType").value("HUMAN"))
                .andExpect(jsonPath("$.data[0].content").value("안녕!"));
    }

    @Test
    @DisplayName("AI 1번 채팅방 원하는 메세지 검색 조회(1번 방을 가진 회원)- 검색어 없을 때")
    void t6() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        Long rooId = 1L;

        String keyword = "없음";

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/rooms/%d/messages/search?query=%s".formatted(rooId, keyword))
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("0개의 메시지를 찾았습니다."))
                // data 배열 크기 확인
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("AI 1번 채팅방 메세지 삭제(1번 방을 가진 회원)")
    void t7() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        Long rooId = 1L;
        Long messageId = 1L;

        ResultActions result = mvc.perform(delete("/api/v1/chat/ai/rooms/%d/messages/%d".formatted(rooId, messageId))
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("1번 메시지를 삭제했습니다."));
    }

    @Test
    @DisplayName("AI 3번 채팅방 메세지 삭제(1번 방을 가진 회원) -> 권한 없음")
    void t8() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        Long rooId = 3L;
        Long messageId = 3L;

        ResultActions result = mvc.perform(delete("/api/v1/chat/ai/rooms/%d/messages/%d".formatted(rooId, messageId))
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("해당 채팅방 메시지에 접근할 권한이 없습니다."));
    }

    @Test
    @DisplayName("AI 5번 채팅방 메세지 삭제(1번 방을 가진 회원) -> 없는 채팅방")
    void t9() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        Long rooId = 5L;
        Long messageId = 3L;

        ResultActions result = mvc.perform(delete("/api/v1/chat/ai/rooms/%d/messages/%d".formatted(rooId, messageId))
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 AI 채팅방입니다."));
    }





}
