package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorChatRoomRepository extends JpaRepository<MentorChatRoom, Long>, MentorChatRoomRepositoryCustom {
    List<MentorChatRoom> findByMenteeAndMenteeLeftFalse(User mentee);
    List<MentorChatRoom> findByMentorNullAndMenteeLeftFalse();
    List<MentorChatRoom> findByMentorAndMenteeLeftFalse(User mentor);
    List<MentorChatRoom> findByMenteeLeftFalse();

    Optional<MentorChatRoom> findByIdAndMenteeLeftFalse(Long roomId);
}
