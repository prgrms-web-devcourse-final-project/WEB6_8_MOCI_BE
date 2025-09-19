package com.moci_3d_backend.domain.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TestService {

    private final Map<String, String> testDataStore = new ConcurrentHashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Object> getTestInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("currentTime", LocalDateTime.now().format(formatter));
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("osName", System.getProperty("os.name"));
        info.put("testDataCount", testDataStore.size());
        info.put("status", "running");
        return info;
    }

    public String createTestData(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        if (count > 100) {
            throw new IllegalArgumentException("Count cannot exceed 100");
        }

        for (int i = 1; i <= count; i++) {
            String key = "test_" + i + "_" + System.currentTimeMillis();
            String value = "Test data " + i + " created at " + LocalDateTime.now().format(formatter);
            testDataStore.put(key, value);
        }

        log.info("Created {} test data entries", count);
        return String.format("Successfully created %d test data entries", count);
    }

    public List<String> getTestData() {
        List<String> result = new ArrayList<>();
        testDataStore.forEach((key, value) -> 
            result.add(String.format("%s: %s", key, value))
        );
        result.sort(String::compareTo);
        return result;
    }

    public String clearTestData() {
        int count = testDataStore.size();
        testDataStore.clear();
        log.info("Cleared {} test data entries", count);
        return String.format("Successfully cleared %d test data entries", count);
    }

    public int getTestDataCount() {
        return testDataStore.size();
    }
}