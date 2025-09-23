package com.moci_3d_backend.global.security;

import lombok.val;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class StompJwtInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // TODO accessToken을 사용한 로그인이 구현되면 주석해제
//        var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        if (accessor == null){
//            return null;
//        }
//        String token = (String) accessor.getSessionAttributes().get("token");
        // TODO accessToken을 통해 유저의 정보 얻어내기

        return message;
    }
}
