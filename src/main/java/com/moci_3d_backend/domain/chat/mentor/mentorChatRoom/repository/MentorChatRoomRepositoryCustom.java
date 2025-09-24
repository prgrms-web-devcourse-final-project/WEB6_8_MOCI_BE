package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;

import java.util.List;

public interface MentorChatRoomRepositoryCustom {

    public List<MentorChatRoomResponse> getMentorChatRooms();
}
