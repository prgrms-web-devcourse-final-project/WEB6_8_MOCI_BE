package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.MentorChatMessage;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class MentorChatRoom {
    @Id
    private Long id;
    private String category;
    private String subCategory;
    @OneToMany(mappedBy = "room")
    List<MentorChatMessage> mentorChatMessageList;
}
