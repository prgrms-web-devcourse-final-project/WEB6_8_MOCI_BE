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
    @DisplayName("멘토 채팅방 입장-실패(잘못된 roomId)")
    void t1_1() throws Exception {
        User user = userService.findByUserId("01023456789");
        String refreshToken = user.getRefreshToken();
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/chat/mentor/mentor/room/join/%d".formatted(-1L))
                                .contentType("application/json")
                                .cookie(new Cookie("refreshToken", refreshToken))
                ).andDo(print());
        resultActions
                .andExpect(handler().handlerType(ApiV1MentorChatRoomController.class))
                .andExpect(handler().methodName("joinMentorChatRoom"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("chat room does not exist"))
        ;
    }

    @Test
    @DisplayName("멘토 채팅방 입장-실패(refresh token 없이)")
    void t1_2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/chat/mentor/mentor/room/join/%d".formatted(-1L))
                                .contentType("application/json")
                ).andDo(print());
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."))
        ;
    }

    @Test
    @DisplayName("멘토 채팅방 입장-실패(멘티의 refresh token)")
    void t1_3() throws Exception {
        User user = userService.findByUserId("01045678901");
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
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.msg").value("권한이 없습니다."))
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
    @DisplayName("입장한 채팅방 조회-실패(refresh token 없이)")
    void t2_1() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/chat/mentor/mentor/room/my-mentees")
                                .contentType("application/json")
                ).andDo(print());
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."))
        ;
    }

    @Test
    @DisplayName("입장한 채팅방 조회-실패(멘티의 refresh token)")
    void t2_2() throws Exception {
        User user = userService.findByUserId("01045678901");
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
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.msg").value("권한이 없습니다."))
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
    @DisplayName("입장 안한 채팅방 조회-실패(멘티의 refresh token)")
    void t3_1() throws Exception {
        User user = userService.findByUserId("01045678901");
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
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.msg").value("권한이 없습니다."))
        ;
    }

    @Test
    @DisplayName("입장 안한 채팅방 조회-실패(refresh token 없이)")
    void t3_2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/chat/mentor/mentor/room/non-mentor-list")
                                .contentType("application/json")
                ).andDo(print());
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."))
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

    @Test
    @DisplayName("전체 채팅방 조회-실패(멘티의 refresh token)")
    void t4_1() throws Exception {
        User user = userService.findByUserId("01045678901");
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
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.msg").value("권한이 없습니다."))
        ;
    }

    @Test
    @DisplayName("전체 채팅방 조회-실패(refresh token 없이)")
    void t4_2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/chat/mentor/mentor/room/all")
                                .contentType("application/json")
                ).andDo(print());
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."))
        ;
    }
}
