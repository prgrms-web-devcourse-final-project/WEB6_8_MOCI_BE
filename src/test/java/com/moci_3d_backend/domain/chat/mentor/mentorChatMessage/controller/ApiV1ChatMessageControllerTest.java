package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApiV1ChatMessageControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private MentorChatMessageService mentorChatMessageService;

    private WebSocketStompClient stompClient;

    @BeforeEach
    void setUp() {
        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        this.stompClient = new WebSocketStompClient(new SockJsClient(transports));
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    @DisplayName("E2E: 메시지 전송 시, DB에 메시지 저장 확인")
    @Transactional
    void sendMessage_E2E_Test() throws Exception {
        // Given: DevInitData에 의해 생성된 실제 유저와 채팅방 사용
        User user = userService.findByUserId("01045678901"); // 박민수
        User mentor = userService.findByUserId("01023456789"); // 김철수
        long roomId = 1L; // DevInitData에 의해 생성된 채팅방 ID
        String token = userService.genAccessToken(user);

        long initialMessageCount = mentorChatMessageService.getChatRoomMessageCount(roomId);

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("Cookie", "accessToken=" + token);

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("roomId", String.valueOf(roomId));

        // When: Connect and send a message
        StompSession session = stompClient.connectAsync(
                String.format("ws://localhost:%d/api/v1/ws", port),
                handshakeHeaders,
                connectHeaders,
                new StompSessionHandlerAdapter() {}
        ).get(5, TimeUnit.SECONDS);

        String messageContent = "E2E Test Message";
        ChatReceiveMessage message = new ChatReceiveMessage(messageContent, 0L);
        StompHeaders sendHeaders = new StompHeaders();
        sendHeaders.setDestination("/api/v1/chat/app/send");
        sendHeaders.add("roomId", String.valueOf(roomId));

        session.send(sendHeaders, message);

        // Then: DB에서 메시지 카운트가 증가했는지 확인
        // 비동기 처리 시간을 고려하여 메시지 저장을 polling으로 확인
        long finalMessageCount = initialMessageCount;
        int maxWaitMillis = 2000;
        int pollIntervalMillis = 100;
        int waited = 0;
        while (waited < maxWaitMillis) {
            finalMessageCount = mentorChatMessageService.getChatRoomMessageCount(roomId);
            if (finalMessageCount == initialMessageCount + 1) {
                break;
            }
            Thread.sleep(pollIntervalMillis);
            waited += pollIntervalMillis;
        }
        assertThat(finalMessageCount).isEqualTo(initialMessageCount + 1);
    }
}