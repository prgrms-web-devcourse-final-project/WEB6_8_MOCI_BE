package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.DetailMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository.MentorChatRoomRepository;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
            throw new IllegalArgumentException("The user is not a mentor of the chat room");
        }
        return mentorChatRoom;
    }

    @Transactional
    public void joinMentorChatRoom(Long roomId, User mentor) {
        MentorChatRoom mentorChatRoom = mentorChatRoomRepository.findByIdAndDeletedFalse(roomId).orElse(null);
        if (mentorChatRoom == null){
            throw new ServiceException(400, "chat room does not exist");
        }
        mentorChatRoom.joinMentor(mentor);
    }


    public List<DetailMentorChatRoom> getDetailMentorChatRooms(){
        List<MentorChatRoom> mentorChatRoomList = mentorChatRoomRepository.findByMentorNullAndDeletedFalse();
        return mentorChatRoomDtoService.toDetailMentorChatRoomList(mentorChatRoomList);
    }

    public List<MentorChatRoomResponse> getMentorChatRooms(User mentor){
        return mentorChatRoomRepository.getMentorChatRooms(mentor, true);
    }

    public List<DetailMentorChatRoom> getAllMentorChatRooms(){
        List<MentorChatRoom> mentorChatRoomList = mentorChatRoomRepository.findByDeletedFalse();
        return mentorChatRoomDtoService.toDetailMentorChatRoomList(mentorChatRoomList);
    }

    public Optional<MentorChatRoom> getChatRoomById(Long roomId) {
        return mentorChatRoomRepository.findByIdAndDeletedFalse(roomId);
    }
}
