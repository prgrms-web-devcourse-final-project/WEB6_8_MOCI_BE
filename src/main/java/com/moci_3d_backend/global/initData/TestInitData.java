package com.moci_3d_backend.global.initData;

import com.moci_3d_backend.domain.test.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile("test")
@RequiredArgsConstructor
@Slf4j
public class TestInitData {
    @Autowired
    @Lazy
    private TestInitData self;
    
    private final TestService testService;

    @Bean
    ApplicationRunner testInitDataApplicationRunner() {
        return args -> {
            self.testDataInit();
        };
    }

    @Transactional
    public void testDataInit() {
        if (testService.getTestDataCount() > 0) {
            return;
        }
        
        log.info("Initializing test data...");
        testService.createTestData(3);
        log.info("Test data initialization completed");
    }
}