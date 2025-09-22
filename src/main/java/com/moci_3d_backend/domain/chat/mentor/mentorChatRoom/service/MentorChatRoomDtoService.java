package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorChatRoomDtoService {

    public MentorChatRoomResponse toMentorChatRoomResponse(MentorChatRoom entity){
        return new MentorChatRoomResponse(entity);
    }

    public Page<MentorChatRoomResponse> toMentorChatRoomResponsePage(Page<MentorChatRoom> page){
        return new PageImpl<>(
                page.stream().map(this::toMentorChatRoomResponse).toList(),
                page.getPageable(),
                page.getTotalElements()
        );
    }
}
