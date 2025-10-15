package com.moci_3d_backend.global.security;

import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.util.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    // 카카오톡 로그인이 성공할 때 마다 이 함수가 실행된다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthUserId = "";
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        String nickname = "";
        String profileImgUrl = "";
        String userId = "";

        switch (providerTypeCode) {
            case "KAKAO" -> {
                Map<String, Object> attributes = oAuth2User.getAttributes();
                Map<String, Object> attributesProperties = (Map<String, Object>) attributes.get("properties");

                oauthUserId = oAuth2User.getName();
                nickname = (String) attributesProperties.get("nickname");
                profileImgUrl = (String) attributesProperties.get("profile_image");
            }
            case "GOOGLE" -> {
                oauthUserId = oAuth2User.getName();
                nickname = (String) oAuth2User.getAttributes().get("name");
                profileImgUrl = (String) oAuth2User.getAttributes().get("picture");
            }
            case "NAVER" -> {
                Map<String, Object> attributes = oAuth2User.getAttributes();
                Map<String, Object> attributesProperties = (Map<String, Object>) attributes.get("response");

                oauthUserId = (String) attributesProperties.get("id");
                nickname = (String) attributesProperties.get("nickname");
                profileImgUrl = (String) attributesProperties.get("profile_image");
            }
        }

        userId = providerTypeCode + "__%s".formatted(oauthUserId);

        User oldUser =  userService.findByUserIdOptional(userId).orElse(null);
        User user;

        if(oldUser != null){
            oldUser.setName(nickname);
            user = userService.save(oldUser);
        }
        else{
            user = new User();
            user.setUserId(userId);
            user.setLoginType(providerTypeCode);
            user.setSocialId(oauthUserId);
            user.setPassword(null);
            user.setRefreshToken(UUID.randomUUID().toString());
            user.setName(nickname);
            user.setEmail(null);
            user.setRole(User.UserRole.USER);
            user.setDigitalLevel(null);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user = userService.save(user);
        }

        return new SecurityUser(
                user.getId(),
                user.getUserId(),
                user.getPassword(),
                user.getName(),  // nickname 파라미터
                user.getAuthorities()
        );
    }
}