package com.moci_3d_backend.domain.chat.ai.aiChatRoom.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AiChatRoomControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mvc;

    //비회원 일때
    @Test
    @DisplayName("AI 채팅방 생성(비회원)")
    void t1() throws Exception {
        String str = """
                {
                    "category": "카테고리",
                    "question": "질문"
                }
                """;

        ResultActions result = mvc.perform(post("/api/v1/chat/ai/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andDo(print());

        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."));
    }

    @Test
    @DisplayName("AI 채팅방 다건 조회(비회원)")
    void t2() throws Exception {
        ResultActions result = mvc.perform(get("/api/v1/chat/ai/room"))
                .andDo(print());

        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."));

    }

    @Test
    @DisplayName("AI 채팅방을 삭제(비회원)")
    void t3() throws Exception {
        ResultActions result = mvc.perform(delete("/api/v1/chat/ai/room/1"))
                .andDo(print());

        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."));
    }


    //회원 일때
    @Test
    @DisplayName("AI 채팅방 생성(회원) 실제 제미나이 호출포함이라 disabled")
    @Disabled("실제 AI 호출 테스트 - 필요할 때만 실행")
    void t4() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        String str = """
                {
                    "category": "카테고리",
                    "question": "안녕"
                }
                """;

        ResultActions result = mvc.perform(post("/api/v1/chat/ai/room")
                        .cookie(new Cookie("refreshToken", refreshToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andDo(print());

        result.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("4번 AI 채팅방이 생성되고 첫 질문이 등록되고 ai 응답을 받았습니다"));

    }

    @Test
    @DisplayName("AI 채팅방 단건 조회(회원)")
    void t5() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/room/1")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.category").value("카카오톡 설명서"))
                .andExpect(jsonPath("$.data.title").value("샘플 AI 채팅방1"))
                .andExpect(jsonPath("$.message").value("1번 AI 채팅방 조회에 성공했습니다."));

    }

    @Test
    @DisplayName("AI 채팅방 단건 조회(회원이 다른 회원의 채팅방 접근시 실패)")
    void t6() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/room/3")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("해당 채팅방에 접근할 권한이 없습니다."));
    }

    @Test
    @DisplayName("AI 채팅방 삭제 조회(자신의 채팅방 삭제)")
    void t7() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        ResultActions result = mvc.perform(delete("/api/v1/chat/ai/room/1")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("1번 AI 채팅방이 삭제되었습니다."));
    }

    @Test
    @DisplayName("AI 자기자신의 채팅방 조회(회원)")
    void t8() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/room/mine")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalRooms").value(2))
                .andExpect(jsonPath("$.message").value("자신의 AI 채팅방 목록 조회에 성공했습니다."));

    }

    //회원인데 관리자권한 접근시
    @Test
    @DisplayName("AI 채팅방 다건 조회(회원이 관리자 권한 접근시 실패)")
    void t9() throws Exception {
        User user1 = userService.findByUserId("01045678901");
        String refreshToken = user1.getRefreshToken();

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/room")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value("관리자만 접근할 수 있습니다."));
    }

    //관리자 일떄
    @Test
    @DisplayName("AI 채팅방 다건 조회(관리자)")
    void t10() throws Exception {
        User user1 = userService.findByUserId("01012345678");
        String refreshToken = user1.getRefreshToken();

        ResultActions result = mvc.perform(get("/api/v1/chat/ai/room")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andDo(print());

        result
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("AI 채팅방 목록 조회에 성공했습니다."));
    }


}
