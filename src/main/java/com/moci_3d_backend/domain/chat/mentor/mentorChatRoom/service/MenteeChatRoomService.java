package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service;

import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.CreateMentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
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
public class MenteeChatRoomService {

    private final MentorChatRoomRepository mentorChatRoomRepository;
    private final MentorChatRoomDtoService mentorChatRoomDtoService;

    @Transactional
    public MentorChatRoomResponse createMenteeChatRoom(CreateMentorChatRoom createMentorChatRoom, User mentee){
        MentorChatRoom mentorChatRoom = new MentorChatRoom(createMentorChatRoom, mentee);

        return mentorChatRoomDtoService.toMentorChatRoomResponse(mentorChatRoomRepository.save(mentorChatRoom));
    }

    public List<MentorChatRoomResponse> getMenteeChatRooms(User mentee){
        return mentorChatRoomRepository.getMentorChatRooms(mentee, false);
    }

    @Transactional
    public void deleteMenteeChatRoom(Long roomId, User mentee){
        MentorChatRoom mentorChatRoom = getMenteeChatRoom(roomId, mentee);
        mentorChatRoom.setDeleted(true);
    }

    public MentorChatRoom getMenteeChatRoom(Long roomId, User mentee){
        MentorChatRoom mentorChatRoom =  mentorChatRoomRepository.findByIdAndDeletedFalse(roomId).orElseThrow(
                () -> new NoSuchElementException("No chat room found with id: " + roomId)
        );
        if (!mentorChatRoom.getMentee().equals(mentee)){
            throw new IllegalArgumentException("The user is not a mentee of the chat room");
        }
        return mentorChatRoom;
    }
}
