package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.DetailMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorChatRoomDtoService {

    public MentorChatRoomResponse toMentorChatRoomResponse(MentorChatRoom entity){
        return new MentorChatRoomResponse(entity);
    }

    public DetailMentorChatRoom toDetailMentorChatRoom(MentorChatRoom entity){
        return new DetailMentorChatRoom(entity);
    }

    public List<MentorChatRoomResponse> toMentorChatRoomResponseList(List<MentorChatRoom> mentorChatRoomList){
        return mentorChatRoomList.stream()
                .map(this::toMentorChatRoomResponse)
                .toList();
    }

    public List<DetailMentorChatRoom> toDetailMentorChatRoomList(List<MentorChatRoom> mentorChatRoomList){
        return mentorChatRoomList.stream()
                .map(this::toDetailMentorChatRoom)
                .toList();
    }

}
