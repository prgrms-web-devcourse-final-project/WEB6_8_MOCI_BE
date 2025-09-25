package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatSendMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.MentorChatMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.repository.MentorChatMessageRepository;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MenteeChatRoomService;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.fileUpload.repository.FileUploadRepository;
import com.moci_3d_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorChatMessageService {

    private final MentorChatMessageRepository mentorChatMessageRepository;
    private final MenteeChatRoomService menteeChatRoomService;
    private final MentorChatRoomService mentorChatRoomService;
    private final FileUploadRepository fileUploadRepository;
    private final MentorChatMessageDtoService mentorChatMessageDtoService;

    private MentorChatRoom getChatRoomByUser(Long roomId, User user){
        return switch (user.getRole()){
            case MENTOR -> mentorChatRoomService.getMentorChatRoom(roomId, user);
            case USER -> menteeChatRoomService.getMenteeChatRoom(roomId, user);
            default -> null;
        };
    }


    @Transactional
    public void saveMentorChatMessage(ChatReceiveMessage message, User sender, Long roomId){
        MentorChatRoom mentorChatRoom = mentorChatRoomService.getMentorChatRoom(roomId, sender);
        FileUpload fileUpload = fileUploadRepository.findById(message.getAttachmentId()).orElse(null);
        MentorChatMessage mentorChatMessage = mentorChatMessageDtoService.toEntity(message, sender, mentorChatRoom, fileUpload);
        mentorChatMessageRepository.save(mentorChatMessage);
    }

    public List<ChatSendMessage> getMentorChatMessages(Long roomId, User user){
        MentorChatRoom mentorChatRoom = getChatRoomByUser(roomId, user);
        if (mentorChatRoom == null){
            throw new IllegalArgumentException("The user is not a member of the chat room");
        }
        List<MentorChatMessage> chats = mentorChatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
        return mentorChatMessageDtoService.toSendMessages(chats);
    }

}
