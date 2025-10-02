package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatSendMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.MentorChatMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.fileUpload.entity.FileUpload;
import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorChatMessageDtoService {

    public ChatSendMessage toSendMessage(MentorChatMessage entity){
        FileUpload attachment = entity.getAttachment();
        return new ChatSendMessage(
                entity.getId(),
                entity.getSender().getName(),
                entity.getContent(),
                attachment != null ? attachment.getId() : null,
                entity.getCreatedAt()
        );
    }

    public MentorChatMessage toEntity(ChatReceiveMessage message, User sender, MentorChatRoom room, FileUpload fileUpload){
        return new MentorChatMessage(
                room,
                sender,
                message.getContent(),
                fileUpload
        );
    }

    public List<ChatSendMessage> toSendMessages(List<MentorChatMessage> entityList){
        return entityList.stream().map(this::toSendMessage).toList();
    }
}
