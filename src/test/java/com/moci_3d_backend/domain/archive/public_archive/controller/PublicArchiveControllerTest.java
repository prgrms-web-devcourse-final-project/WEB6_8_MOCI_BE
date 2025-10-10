package com.moci_3d_backend.domain.archive.public_archive.controller;

import com.moci_3d_backend.domain.archive.public_archive.entity.PublicArchive;
import com.moci_3d_backend.domain.archive.public_archive.repository.PublicArchiveRepository;
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
public class PublicArchiveControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PublicArchiveRepository publicArchiveRepository;

    // ========================================
    // Public API 테스트 (인증 불필요)
    // ========================================

    @Test
    @DisplayName("공개 자료실 전체 목록 조회")
    void t1() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive/public")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(PublicArchiveController.class))
                .andExpect(handler().methodName("getPublicArchives"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("교육자료실 목록 조회 성공"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.archives").isArray());
    }

    @Test
    @DisplayName("공개 자료실 카테고리 필터링 조회 - KAKAO_TALK")
    void t2() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive/public")
                                .param("category", "KAKAO_TALK")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("교육자료실 목록 조회 성공"))
                .andExpect(jsonPath("$.data.archives").isArray());
    }

    @Test
    @DisplayName("공개 자료실 키워드 검색 - '카카오톡'")
    void t3() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive/public")
                                .param("keyword", "카카오톡")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.archives").isArray());
    }

    @Test
    @DisplayName("공개 자료실 카테고리 + 키워드 복합 검색")
    void t4() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive/public")
                                .param("category", "KAKAO_TALK")
                                .param("keyword", "친구")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.archives").isArray());
    }

    @Test
    @DisplayName("공개 자료실 상세 조회")
    void t5() throws Exception {
        // given
        PublicArchive archive = publicArchiveRepository.findAll().getFirst();

        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive/public/{archiveId}", archive.getId())
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(PublicArchiveController.class))
                .andExpect(handler().methodName("getPublicArchive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("교육 자료실 상세조회 성공"))
                .andExpect(jsonPath("$.data.id").value(archive.getId()))
                .andExpect(jsonPath("$.data.title").exists());
    }

    @Test
    @DisplayName("공개 자료실 상세 조회 - 존재하지 않는 ID (실패)")
    void t6() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/archive/public/{archiveId}", 99999L)
                )
                .andDo(print());

        // then
        // EntityNotFoundException이 GlobalExceptionHandler에서 처리되지 않아 500 에러 발생
        resultActions
                .andExpect(status().isNotFound());
    }

    // ========================================
    // Admin API 테스트 (인증 필요)
    // ========================================

    @Test
    @DisplayName("[관리자] 공개 자료실 생성 - 성공")
    void t7() throws Exception {
        // given
        User admin = userService.findByUserId("01012345678");
        String refreshToken = admin.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/admin/archive/public")
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "새로운 자료",
                                            "description": "새로운 자료 설명",
                                            "category": "BAEMIN",
                                            "subCategory": "주문",
                                            "fileIds": null
                                        }
                                        """)
                )
                .andDo(print());

        // then
        // HTTP 상태 코드는 200, 응답 본문의 code는 201
        resultActions
                .andExpect(handler().handlerType(PublicArchiveController.class))
                .andExpect(handler().methodName("createPublicArchive"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("교육 자료실 글 생성 성공 "))
                .andExpect(jsonPath("$.data.title").value("새로운 자료"))
                .andExpect(jsonPath("$.data.category").value("BAEMIN"));
    }

    @Test
    @DisplayName("[관리자] 공개 자료실 생성 - 제목 없음(실패)")
    void t8() throws Exception {
        // given
        User admin = userService.findByUserId("01012345678");
        String refreshToken = admin.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/admin/archive/public")
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "",
                                            "description": "설명",
                                            "category": "BAEMIN"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[관리자] 공개 자료실 수정 - 성공")
    void t9() throws Exception {
        // given
        User admin = userService.findByUserId("01012345678");
        String refreshToken = admin.getRefreshToken();
        PublicArchive archive = publicArchiveRepository.findAll().get(0);

        // when
        ResultActions resultActions = mvc
                .perform(
                        put("/api/v1/admin/archive/public/{archiveId}", archive.getId())
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "수정된 제목",
                                            "description": "수정된 설명",
                                            "category": "YOUTUBE",
                                            "subCategory": "업로드",
                                            "fileIds": null
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(PublicArchiveController.class))
                .andExpect(handler().methodName("updatePublicArchive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("교육 자료실 글 수정 성공"))
                .andExpect(jsonPath("$.data.title").value("수정된 제목"));
    }

    @Test
    @DisplayName("[관리자] 공개 자료실 삭제 - 성공")
    void t10() throws Exception {
        // given
        User admin = userService.findByUserId("01012345678");
        String refreshToken = admin.getRefreshToken();
        PublicArchive archive = publicArchiveRepository.findAll().getFirst();

        // when
        ResultActions resultActions = mvc
                .perform(
                        delete("/api/v1/admin/archive/public/{archiveId}", archive.getId())
                                .cookie(new Cookie("refreshToken", refreshToken))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(handler().handlerType(PublicArchiveController.class))
                .andExpect(handler().methodName("deletePublicArchive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("교육 자료실 글 삭제 성공"));
    }

    @Test
    @DisplayName("[관리자] 공개 자료실 생성 - refreshToken 없이(실패)")
    void t11() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/admin/archive/public")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "권한 없는 생성",
                                            "description": "설명",
                                            "category": "KAKAO_TALK"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value("로그인이 필요합니다."));
    }

    @Test
    @DisplayName("[일반 사용자] 공개 자료실 생성 시도 - 권한 없음(실패)")
    void t12() throws Exception {
        // given
        User normalUser = userService.findByUserId("01045678901"); // 일반 사용자
        String refreshToken = normalUser.getRefreshToken();

        // when
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/admin/archive/public")
                                .cookie(new Cookie("refreshToken", refreshToken))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "권한 없는 생성",
                                            "description": "설명",
                                            "category": "KAKAO_TALK"
                                        }
                                        """)
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isForbidden());
    }
}