package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.user.entity.User;

import java.util.List;

public interface MentorChatRoomRepositoryCustom {

    List<MentorChatRoomResponse> getMentorChatRooms(User user, boolean isMentor);
}
