package com.moci_3d_backend.domain.archive.archive_request.controller;

import com.moci_3d_backend.domain.archive.archive_request.entity.ArchiveRequest;
import com.moci_3d_backend.domain.archive.archive_request.entity.RequestStatus;
import com.moci_3d_backend.domain.archive.archive_request.repository.ArchiveRequestRepository;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ArchiveRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ArchiveRequestRepository archiveRequestRepository;

    // ========================================
    // Mentor API 테스트 (멘토 권한 필요)
    // ========================================

    @Test
    @DisplayName("[멘토] 자료 요청글 생성")
    void t1() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/archive-requests")
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "새로운 자료 요청",
                                            "description": "이 자료가 필요합니다"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(ArchiveRequestController.class))
                .andExpect(handler().methodName("createArchiveRequest"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("자료 요청글이 생성되었습니다."))
                .andExpect(jsonPath("$.data.title").value("새로운 자료 요청"));
    }

    @Test
    @DisplayName("[멘토] 자료 요청글 생성 - 제목 없음(실패)")
    void t2() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/archive-requests")
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "",
                                            "description": "설명"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[멘토] 자료 요청글 수정")
    void t3() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();
        ArchiveRequest request = archiveRequestRepository.findAll().stream()
                .filter(r -> r.getUser().getId().equals(mentor.getId()))
                .findFirst()
                .orElseThrow();

        // when
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/archive-requests/{requestId}", request.getId())
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "수정된 제목",
                                            "description": "수정된 설명"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(ArchiveRequestController.class))
                .andExpect(handler().methodName("updateArchiveRequest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("자료 요청글이 수정되었습니다."))
                .andExpect(jsonPath("$.data.title").value("수정된 제목"));
    }

    @Test
    @DisplayName("[멘토] 자료 요청글 삭제")
    void t4() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();
        ArchiveRequest request = archiveRequestRepository.findAll().stream()
                .filter(r -> r.getUser().getId().equals(mentor.getId()))
                .findFirst()
                .orElseThrow();

        // when
        ResultActions resultActions = mvc
                .perform(
                        delete("/api/v1/archive-requests/{requestId}", request.getId())
                                .cookie(new Cookie("refreshToken", refreshToken))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(ArchiveRequestController.class))
                .andExpect(handler().methodName("deleteArchiveRequest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("자료 요청글이 성공적으로 삭제되었습니다."));
    }

    @Test
    @DisplayName("[일반 사용자] 자료 요청글 생성 시도 - 권한 없음(실패)")
    void t5() throws Exception {
        // given
        User normalUser = userService.findByUserId("01045678901");
        String refreshToken = normalUser.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/archive-requests")
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "권한 없는 요청",
                                            "description": "설명"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isForbidden());
    }

    // ========================================
    // Admin & Mentor API 테스트
    // ========================================

    @Test
    @DisplayName("[멘토] 자료 요청 목록 조회")
    void t6() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive-requests")
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .param("page", "0")
                                .param("size", "10")
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(ArchiveRequestController.class))
                .andExpect(handler().methodName("getArchiveRequests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("자료 요청 목록을 성공적으로 조회했습니다."))
                .andExpect(jsonPath("$.data.requests").isArray());
    }

    @Test
    @DisplayName("[멘토] 자료 요청 목록 조회 - 상태 필터(PENDING)")
    void t7() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive-requests")
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .param("status", "PENDING")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.requests").isArray());
    }

    @Test
    @DisplayName("[멘토] 자료 요청 상세 조회")
    void t8() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();
        ArchiveRequest request = archiveRequestRepository.findAll().getFirst();

        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive-requests/{requestId}", request.getId())
                                .cookie(new Cookie("refreshToken", refreshToken))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(ArchiveRequestController.class))
                .andExpect(handler().methodName("getArchiveRequest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("자료 요청 상세 정보를 성공적으로 조회했습니다."))
                .andExpect(jsonPath("$.data.id").value(request.getId()));
    }

    @Test
    @DisplayName("[멘토] 사용자별 자료 요청 목록 조회")
    void t9() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();
        Long targetUserId = mentor.getId(); // mentor1 ID

        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/users/{userId}/archive-requests", targetUserId)
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .param("page", "0")
                                .param("size", "10")
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(ArchiveRequestController.class))
                .andExpect(handler().methodName("getUserArchiveRequestByUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("사용자별 자료 요청 목록을 성공적으로 조회했습니다."))
                .andExpect(jsonPath("$.data.requests").isArray());
    }

    @Test
    @DisplayName("[일반 사용자] 자료 요청 목록 조회 - 권한 없음(실패)")
    void t10() throws Exception {
        // given
        User normalUser = userService.findByUserId("01045678901");
        String refreshToken = normalUser.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive-requests")
                                .cookie(new Cookie("refreshToken", refreshToken))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isForbidden());
    }

    // ========================================
    // Admin API 테스트 (관리자 권한 필요)
    // ========================================

    @Test
    @DisplayName("[관리자] 자료 요청 상태 변경 - APPROVED")
    void t11() throws Exception {
        // given
        User admin = userService.findByUserId("01012345678");
        String refreshToken = admin.getRefreshToken();
        ArchiveRequest request = archiveRequestRepository.findAll().stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .findFirst()
                .orElseThrow();

        // when
        ResultActions resultActions = mvc
                .perform(
                        patch("/api/v1/archive-requests/{requestId}/status", request.getId())
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "status": "APPROVED"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(ArchiveRequestController.class))
                .andExpect(handler().methodName("updateArchiveRequestStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("자료 요청이 성공적으로 승인 되었습니다."))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));
    }

    @Test
    @DisplayName("[관리자] 자료 요청 상태 변경 - REJECTED")
    void t12() throws Exception {
        // given
        User admin = userService.findByUserId("01012345678");
        String refreshToken = admin.getRefreshToken();
        ArchiveRequest request = archiveRequestRepository.findAll().stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .findFirst()
                .orElseThrow();

        // when
        ResultActions resultActions = mvc
                .perform(
                        patch("/api/v1/archive-requests/{requestId}/status", request.getId())
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "status": "REJECTED"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("자료 요청이 성공적으로 거절 되었습니다."))
                .andExpect(jsonPath("$.data.status").value("REJECTED"));
    }

    @Test
    @DisplayName("[관리자] 대기중인 자료 요청 개수 조회")
    void t13() throws Exception {
        // given
        User admin = userService.findByUserId("01012345678");
        String refreshToken = admin.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive-requests/pending/count")
                                .cookie(new Cookie("refreshToken", refreshToken))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(ArchiveRequestController.class))
                .andExpect(handler().methodName("getPendingRequestCount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("대기중인 자료 요청 개수를 성공적으로 조회했습니다."))
                .andExpect(jsonPath("$.data").isNumber());
    }

    @Test
    @DisplayName("[멘토] 관리자 API 접근 시도 - 권한 없음(실패)")
    void t14() throws Exception {
        // given
        User mentor = userService.findByUserId("01023456789");
        String refreshToken = mentor.getRefreshToken();
        ArchiveRequest request = archiveRequestRepository.findAll().getFirst();

        // when
        ResultActions resultActions = mvc
                .perform(
                        patch("/api/v1/archive-requests/{requestId}/status", request.getId())
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "status": "APPROVED"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[인증 없음] 자료 요청글 생성 시도(실패)")
    void t15() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/archive-requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "인증 없는 요청",
                                            "description": "설명"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."));
    }
}
