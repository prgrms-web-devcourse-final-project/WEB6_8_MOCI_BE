package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository.MentorChatRoomRepository;
import com.moci_3d_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MentorChatRoomService {

    private final MentorChatRoomRepository mentorChatRoomRepository;
    private final MentorChatRoomDtoService mentorChatRoomDtoService;

    @Transactional
    public void createMentorChatRoom(String category, String subCategory, User mentee){
        MentorChatRoom mentorChatRoom = new MentorChatRoom(category, subCategory, mentee);
        mentorChatRoomRepository.save(mentorChatRoom);
    }

    public Page<MentorChatRoomResponse> getMentorChatRooms(User user, Pageable pageable){
        Page<MentorChatRoom> mentorChatRoomPage = mentorChatRoomRepository.findByMenteeAndDeletedFalse(user, pageable);
        return mentorChatRoomDtoService.toMentorChatRoomResponsePage(mentorChatRoomPage);
    }

    @Transactional
    public void deleteMentorChatRoom(Long roomId, User user){
        MentorChatRoom mentorChatRoom = mentorChatRoomRepository.findByIdAndDeletedFalse(roomId).orElseThrow(
                () -> new NoSuchElementException("No chat room found with id: " + roomId)
        );
        if (!mentorChatRoom.getMentee().equals(user)){
            throw new IllegalArgumentException("The user is not a mentee of the chat room");
        }
        mentorChatRoom.setDeleted(true);
    }

    public MentorChatRoom getMentorChatRoom(Long roomId, User user){
        MentorChatRoom mentorChatRoom =  mentorChatRoomRepository.findByIdAndDeletedFalse(roomId).orElseThrow(
                () -> new NoSuchElementException("No chat room found with id: " + roomId)
        );
        if (!mentorChatRoom.getMentee().equals(user)){
            throw new IllegalArgumentException("The user is not a mentee of the chat room");
        }
        return mentorChatRoom;
    }
}
