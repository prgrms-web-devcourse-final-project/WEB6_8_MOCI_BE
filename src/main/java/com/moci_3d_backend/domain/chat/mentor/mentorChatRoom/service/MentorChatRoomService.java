package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.DetailMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository.MentorChatRoomRepository;
import com.moci_3d_backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MentorChatRoomService {

    private final MentorChatRoomRepository mentorChatRoomRepository;
    private final MentorChatRoomDtoService mentorChatRoomDtoService;

    public MentorChatRoom getMentorChatRoom(Long roomId, User user){
        MentorChatRoom mentorChatRoom =  mentorChatRoomRepository.findByIdAndDeletedFalse(roomId).orElseThrow(
                () -> new NoSuchElementException("No chat room found with id: " + roomId)
        );
        if (!mentorChatRoom.getMentor().equals(user)){
            throw new IllegalArgumentException("The user is not a mentee of the chat room");
        }
        return mentorChatRoom;
    }

    @Transactional
    public void joinMentorChatRoom(Long roomId, User mentor) {
        MentorChatRoom mentorChatRoom = getMentorChatRoom(roomId, mentor);
        mentorChatRoom.joinMentor(mentor);
    }


    public List<DetailMentorChatRoom> getDetailMentorChatRooms(){
        List<MentorChatRoom> mentorChatRoomList = mentorChatRoomRepository.findByMentorNullAndDeletedFalse();
        return mentorChatRoomDtoService.toDetailMentorChatRoomList(mentorChatRoomList);
    }

    public List<MentorChatRoomResponse> getMentorChatRooms(User mentor){
        List<MentorChatRoom> mentorChatRoomList = mentorChatRoomRepository.findByMentorAndDeletedFalse(mentor);
        return mentorChatRoomDtoService.toMentorChatRoomResponseList(mentorChatRoomList);
    }
}
