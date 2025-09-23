package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.MentorChatMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.repository.MentorChatMessageRepository;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.fileUpload.repository.FileUploadRepository;
import com.moci_3d_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MentorChatMessageService {

    private final MentorChatMessageRepository mentorChatMessageRepository;
    private final MentorChatRoomService mentorChatRoomService;
    private final FileUploadRepository fileUploadRepository;

    @Transactional
    public void saveMentorChatMessage(ChatMessage message, User sender, Long roomId){
        MentorChatRoom mentorChatRoom = mentorChatRoomService.getMentorChatRoom(roomId, sender);
        FileUpload fileUpload = fileUploadRepository.findById(message.getAttachmentId()).orElse(null);
        MentorChatMessage mentorChatMessage = new MentorChatMessage(
                mentorChatRoom,
                sender,
                message.getContent(),
                fileUpload
        );
        mentorChatMessageRepository.save(mentorChatMessage);
    }

}
