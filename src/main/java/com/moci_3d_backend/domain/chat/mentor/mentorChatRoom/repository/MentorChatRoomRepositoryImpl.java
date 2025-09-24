package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.QMentorChatMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.QMentorChatRoom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MentorChatRoomRepositoryImpl implements MentorChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<MentorChatRoomResponse> getMentorChatRooms() {
        QMentorChatRoom chatRoom = QMentorChatRoom.mentorChatRoom;
        QMentorChatMessage message = QMentorChatMessage.mentorChatMessage;


        List<MentorChatRoomResponse> result =  jpaQueryFactory
                .select(Projections.bean(MentorChatRoomResponse.class,
                        chatRoom.id,
                        chatRoom.title,
                        message.count().as("unread_count")
                ))
                .from(chatRoom)
                .leftJoin(message).on(
                        message.room.eq(chatRoom)
                                .and(message.createdAt.after(chatRoom.mentorLastAt))
                )
                .where(chatRoom.deleted.eq(false))
                .groupBy(chatRoom.id)
                .fetch();
        return result;

    }
}
