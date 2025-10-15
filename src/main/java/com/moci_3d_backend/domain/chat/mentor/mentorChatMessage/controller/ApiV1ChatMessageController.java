package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import com.moci_3d_backend.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ApiV1ChatMessageController {

    private final MentorChatMessageService mentorChatMessageService;
    private final UserService userService;

    @MessageMapping("send")
    public void sendMessage(
            ChatReceiveMessage message,
            StompHeaderAccessor accessor,
            Principal principal
    ) {
        String roomIdStr = accessor.getFirstNativeHeader("roomId");
        if (roomIdStr == null){
            throw new IllegalArgumentException("roomId is null");
        }
        Long roomId = Long.parseLong(roomIdStr);
        SecurityUser securityUser = (SecurityUser) principal;
        User user = new User();
        user.setId(securityUser.getId());
        user.setName(securityUser.getNickname());
        if (securityUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MENTOR"))){
            user.setRole(User.UserRole.MENTOR);
        }else if (securityUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))){
            user.setRole(User.UserRole.USER);
        }else if (securityUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            user.setRole(User.UserRole.ADMIN);
        }else{
            throw new IllegalArgumentException("User role is not supported");
        }

        mentorChatMessageService.sendMessage(roomId, message, Optional.of(user));
    }
}
