package com.moci_3d_backend.domain.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TestService 단위 테스트")
class TestServiceTest {

    @InjectMocks
    private TestService testService;

    @BeforeEach
    void setUp() {
        testService.clearTestData();
    }

    @Test
    @DisplayName("테스트 정보 조회 - 성공")
    void getTestInfo_Success() {
        // when
        Map<String, Object> info = testService.getTestInfo();

        // then
        assertThat(info).isNotNull();
        assertThat(info).containsKeys("currentTime", "javaVersion", "osName", "testDataCount", "status");
        assertThat(info.get("status")).isEqualTo("running");
        assertThat(info.get("testDataCount")).isEqualTo(0);
    }

    @Test
    @DisplayName("테스트 데이터 생성 - 성공")
    void createTestData_Success() {
        // given
        int count = 3;

        // when
        String result = testService.createTestData(count);

        // then
        assertThat(result).contains("Successfully created 3 test data entries");
        assertThat(testService.getTestDataCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("테스트 데이터 생성 - 음수 개수로 실패")
    void createTestData_FailWithNegativeCount() {
        // when & then
        assertThatThrownBy(() -> testService.createTestData(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Count must be positive");
    }

    @Test
    @DisplayName("테스트 데이터 생성 - 100개 초과로 실패")
    void createTestData_FailWithTooManyCount() {
        // when & then
        assertThatThrownBy(() -> testService.createTestData(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Count cannot exceed 100");
    }

    @Test
    @DisplayName("테스트 데이터 조회 - 성공")
    void getTestData_Success() {
        // given
        testService.createTestData(2);

        // when
        List<String> data = testService.getTestData();

        // then
        assertThat(data).hasSize(2);
        assertThat(data).allMatch(item -> item.contains("Test data"));
    }

    @Test
    @DisplayName("테스트 데이터 삭제 - 성공")
    void clearTestData_Success() {
        // given
        testService.createTestData(5);
        assertThat(testService.getTestDataCount()).isEqualTo(5);

        // when
        String result = testService.clearTestData();

        // then
        assertThat(result).contains("Successfully cleared 5 test data entries");
        assertThat(testService.getTestDataCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("테스트 데이터 개수 조회 - 성공")
    void getTestDataCount_Success() {
        // given
        testService.createTestData(7);

        // when
        int count = testService.getTestDataCount();

        // then
        assertThat(count).isEqualTo(7);
    }
}