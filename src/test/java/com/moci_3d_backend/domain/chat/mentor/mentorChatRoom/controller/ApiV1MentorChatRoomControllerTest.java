package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ApiV1MentorChatRoomControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("멘토 채팅방 입장")
    void t1() throws Exception {
        User user = userService.findByUserId("01023456789");
        String refreshToken = user.getRefreshToken();
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/chat/mentor/mentor/room/join/%d".formatted(2L))
                                .contentType("application/json")
                                .cookie(new Cookie("refreshToken", refreshToken))
                ).andDo(print());
        resultActions
                .andExpect(handler().handlerType(ApiV1MentorChatRoomController.class))
                .andExpect(handler().methodName("joinMentorChatRoom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success to join mentor chat room"))
                ;
    }

    @Test
    @DisplayName("입장한 채팅방 조회")
    void t2() throws Exception {
        User user = userService.findByUserId("01023456789");
        String refreshToken = user.getRefreshToken();
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/chat/mentor/mentor/room/my-mentees")
                                .contentType("application/json")
                                .cookie(new Cookie("refreshToken", refreshToken))
                ).andDo(print());
        resultActions
                .andExpect(handler().handlerType(ApiV1MentorChatRoomController.class))
                .andExpect(handler().methodName("getMyMenteeChatRooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success to load my mentee chat rooms"))
                ;
    }

    @Test
    @DisplayName("입장 안한 채팅방 조회")
    void t3() throws Exception {
        User user = userService.findByUserId("01023456789");
        String refreshToken = user.getRefreshToken();
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/chat/mentor/mentor/room/non-mentor-list")
                                .contentType("application/json")
                                .cookie(new Cookie("refreshToken", refreshToken))
                ).andDo(print());
        resultActions
                .andExpect(handler().handlerType(ApiV1MentorChatRoomController.class))
                .andExpect(handler().methodName("getMentorChatRooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success to load chat rooms"))
        ;
    }

    @Test
    @DisplayName("전체 채팅방 조회")
    void t4() throws Exception {
        User user = userService.findByUserId("01023456789");
        String refreshToken = user.getRefreshToken();
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/chat/mentor/mentor/room/all")
                                .contentType("application/json")
                                .cookie(new Cookie("refreshToken", refreshToken))
                ).andDo(print());
        resultActions
                .andExpect(handler().handlerType(ApiV1MentorChatRoomController.class))
                .andExpect(handler().methodName("getAllChatRooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("success to load chat rooms"))
        ;
    }
    
}
