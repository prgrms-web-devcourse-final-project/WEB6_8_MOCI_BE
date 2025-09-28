package com.moci_3d_backend.domain.user.service;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.external.util.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {
    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${custom.accessToken.expirationSeconds}")
    private int accessTokenExpirationSeconds;

    String genAccessToken(User member) {
        long id = member.getId();
        String userId = member.getUserId();
        String name = member.getName();

        return Ut.jwt.toString(
                jwtSecretKey,
                accessTokenExpirationSeconds,
                Map.of("id", id, "userId", userId, "name", name)
        );
    }

    Map<String, Object> payload(String accessToken) {
        Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, accessToken);

        if (parsedPayload == null) return null;

        int id = (int) parsedPayload.get("id");
        String userId = (String) parsedPayload.get("userId");
        String name = (String) parsedPayload.get("name");

        return Map.of("id", id, "userId", userId, "name", name);
    }
}