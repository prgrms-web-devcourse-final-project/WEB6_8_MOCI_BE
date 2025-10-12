package com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.repository;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.entity.QMentorChatMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.MentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.dto.QMentorChatRoomResponse;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.QMentorChatRoom;
import com.moci_3d_backend.domain.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MentorChatRoomRepositoryImpl implements MentorChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<MentorChatRoomResponse> getMentorChatRooms(User user, boolean isMentor) {
        QMentorChatRoom chatRoom = QMentorChatRoom.mentorChatRoom;
        QMentorChatMessage message = QMentorChatMessage.mentorChatMessage;

        BooleanBuilder builder = new BooleanBuilder();
        BooleanBuilder joinBuilder = new BooleanBuilder();
        joinBuilder.and(message.room.eq(chatRoom));

        if (user != null){
            if (isMentor){
                builder.and(chatRoom.mentor.eq(user));
                builder.and(chatRoom.mentorLeft.isFalse());
                joinBuilder.and(message.createdAt.after(chatRoom.mentorLastAt));
            }else{
                builder.and(chatRoom.mentee.eq(user));
                builder.and(chatRoom.menteeLeft.isFalse());
                joinBuilder.and(message.createdAt.after(chatRoom.menteeLastAt));
            }
        }else{
            // 만들어두긴 했지만 현재기준 안쓰임
            builder.and(chatRoom.mentor.isNull());
        }

        return jpaQueryFactory
                .select(new QMentorChatRoomResponse(
                        chatRoom.id,
                        chatRoom.category,
                        chatRoom.question,
                        message.count().as("unread_count"),
                        chatRoom.createdAt,
                        chatRoom.menteeLeft,
                        chatRoom.mentorLeft
                ))
                .from(chatRoom)
                .leftJoin(message).on(
                        joinBuilder
                )
                .where(builder)
                .groupBy(chatRoom.id)
                .fetch();
    }
}
