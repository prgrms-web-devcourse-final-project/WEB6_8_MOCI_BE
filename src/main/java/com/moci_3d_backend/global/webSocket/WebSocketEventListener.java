package com.moci_3d_backend.global.webSocket;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository.MentorChatRoomRepository;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final MentorChatRoomService mentorChatRoomService;
    private final MentorChatRoomRepository mentorChatRoomRepository;

    @EventListener
    public void handlerSessionDisconnect(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Long roomId = (Long) accessor.getSessionAttributes().getOrDefault("roomId", null);
        if (roomId == null){
            return;
        }
        MentorChatRoom room = mentorChatRoomService.getChatRoomById(roomId).orElse(null);
        if (room == null){
            return;
        }
        User mentee = room.getMentee();
        User mentor = room.getMentor();
        SecurityUser user = (SecurityUser) accessor.getSessionAttributes().getOrDefault("user", null);
        if (user == null){
            return;
        }
        if (mentee.getId().equals(user.getId())){
            room.updateMenteeLastAt();
            mentorChatRoomRepository.save(room);
            return;
        }

        if (mentor.getId().equals(user.getId())){
            room.updateMentorLastAt();
            mentorChatRoomRepository.save(room);
            return;
        }
        return;
    }
}
