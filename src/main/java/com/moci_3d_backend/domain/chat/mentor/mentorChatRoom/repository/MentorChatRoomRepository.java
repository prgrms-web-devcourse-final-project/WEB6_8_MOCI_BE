package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorChatRoomRepository extends JpaRepository<MentorChatRoom, Long> {
    List<MentorChatRoom> findByMenteeAndDeletedFalse(User mentee);
    List<MentorChatRoom> findByMentorNullAndDeletedFalse();
    List<MentorChatRoom> findByMentorAndDeletedFalse(User mentor);

    Optional<MentorChatRoom> findByIdAndDeletedFalse(Long roomId);
}
