package com.moci_3d_backend.global.security;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class StompJwtInterceptor implements ChannelInterceptor {

    private final MentorChatRoomService mentorChatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();
        if (command == null) {
            return message;
        }
        SecurityUser user = (SecurityUser) accessor.getSessionAttributes().getOrDefault("user", null);
        if (user == null) {
            return message;
        }
        accessor.setUser(user);
        if (command == StompCommand.CONNECT) {
            String roomIdStr = accessor.getFirstNativeHeader("roomId");
            if (roomIdStr == null){
                throw new IllegalArgumentException("roomId is null");
            }
            Long roomId = Long.parseLong(roomIdStr);
            MentorChatRoom room = mentorChatRoomService.getChatRoomById(roomId).orElseThrow();
            User mentee = room.getMentee();
            User mentor = room.getMentor();
            if (!mentee.getId().equals(user.getId()) && !mentor.getId().equals(user.getId())){
                throw new IllegalArgumentException("user is not mentee or mentor");
            }
            accessor.getSessionAttributes().put("roomId", roomId);
        }

        return message;
    }
}
