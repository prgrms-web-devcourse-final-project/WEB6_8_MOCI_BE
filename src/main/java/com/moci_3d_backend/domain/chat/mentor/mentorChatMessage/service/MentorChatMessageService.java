package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.repository.MentorChatMessageRepository;
import com.moci_3d_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorChatMessageService {

    private final MentorChatMessageRepository mentorChatMessageRepository;

    public void saveMentorChatMessage(ChatMessage message, User user){

    }

}
