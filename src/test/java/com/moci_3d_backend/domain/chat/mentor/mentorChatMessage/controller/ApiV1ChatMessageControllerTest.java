package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatReceiveMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.dto.ChatSendMessage;
import com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.service.MentorChatMessageService;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.entity.MentorChatRoom;
import com.moci_3d_backend.domain.chat.mentor.mentorChatRoom.service.MentorChatRoomService;
import com.moci_3d_backend.domain.user.entity.User;
import com.moci_3d_backend.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiV1ChatMessageControllerTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private MentorChatMessageService mentorChatMessageService;

    @MockitoBean
    private MentorChatRoomService mentorChatRoomService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private WebSocketStompClient stompClient;

    @BeforeEach
    void setUp() {
        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        this.stompClient = new WebSocketStompClient(new SockJsClient(transports));
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    @DisplayName("웹소켓 메시지 전송 시, 서비스에 올바른 데이터가 전달되는지 확인")
    void sendMessageIntegrationTest() throws ExecutionException, InterruptedException, TimeoutException {
        // Given: Mocking setup
        long userId = 1L;
        String userName = "testUser";
        long roomId = 1L;
        String token = "test-jwt-token";

        User mentor = new User();
        mentor.setId(userId);
        mentor.setName(userName);

        User mentee = new User();
        mentee.setId(2L);

        MentorChatRoom chatRoom = new MentorChatRoom();
        chatRoom.setMentor(mentor);
        chatRoom.setMentee(mentee);

        when(userService.payload(token)).thenReturn(Map.of("id", (int)userId, "userId", "testUser", "name", userName, "role", "USER"));
        when(mentorChatRoomService.getChatRoomById(roomId)).thenReturn(Optional.of(chatRoom));

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

        String messageContent = "Hello ArgumentCaptor!";
        ChatReceiveMessage message = new ChatReceiveMessage(messageContent, 0L);
        StompHeaders sendHeaders = new StompHeaders();
        sendHeaders.setDestination("/api/v1/chat/app/send");
        sendHeaders.add("roomId", String.valueOf(roomId));

        session.send(sendHeaders, message);

        // Then: Capture the arguments passed to the service method
        ArgumentCaptor<Long> roomIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<ChatReceiveMessage> messageCaptor = ArgumentCaptor.forClass(ChatReceiveMessage.class);
        ArgumentCaptor<Optional<User>> userCaptor = ArgumentCaptor.forClass(Optional.class);

        verify(mentorChatMessageService, timeout(2000)).sendMessage(
                roomIdCaptor.capture(),
                messageCaptor.capture(),
                userCaptor.capture()
        );

        // And: Assert the captured arguments are correct
        assertThat(roomIdCaptor.getValue()).isEqualTo(roomId);
        assertThat(messageCaptor.getValue().getContent()).isEqualTo(messageContent);
        assertThat(userCaptor.getValue()).isPresent();
        assertThat(userCaptor.getValue().get().getId()).isEqualTo(userId);
        assertThat(userCaptor.getValue().get().getName()).isEqualTo(userName);
    }

    @Test
    @DisplayName("t2: 메시지 전송 후, 구독한 토픽에서 메시지 수신 확인")
    void receiveMessageTest() throws Exception {
        // Given
        long userId = 1L;
        long roomId = 1L;
        String userName = "testUser";
        String token = "test-jwt-token";
        String messageContent = "Hello Subscriber!";

        User mentor = new User();
        mentor.setId(userId);
        mentor.setName(userName);
        User mentee = new User();
        mentee.setId(2L);
        MentorChatRoom chatRoom = new MentorChatRoom();
        chatRoom.setMentor(mentor);
        chatRoom.setMentee(mentee);

        when(userService.payload(token)).thenReturn(Map.of("id", (int)userId, "userId", "testUser", "name", userName, "role", "USER"));
        when(mentorChatRoomService.getChatRoomById(roomId)).thenReturn(Optional.of(chatRoom));

        // We need to mock the service's response when it's called by the controller
        ChatSendMessage sentMessage = new ChatSendMessage(1L, userName, messageContent, 0L, java.time.LocalDateTime.now());
        doAnswer(invocation -> {
            messagingTemplate.convertAndSend("/api/v1/chat/topic/" + roomId, sentMessage);
            return null;
        }).when(mentorChatMessageService).sendMessage(any(), any(), any());


        CompletableFuture<ChatSendMessage> future = new CompletableFuture<>();

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("Cookie", "accessToken=" + token);

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("roomId", String.valueOf(roomId));

        // When
        StompSession session = stompClient.connectAsync(
                String.format("ws://localhost:%d/api/v1/ws", port),
                handshakeHeaders,
                connectHeaders,
                new StompSessionHandlerAdapter() {}
        ).get(5, TimeUnit.SECONDS);

        session.subscribe("/api/v1/chat/topic/" + roomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatSendMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                future.complete((ChatSendMessage) payload);
            }
        });

        ChatReceiveMessage messageToSend = new ChatReceiveMessage(messageContent, 0L);
        StompHeaders sendHeaders = new StompHeaders();
        sendHeaders.setDestination("/api/v1/chat/app/send");
        sendHeaders.add("roomId", String.valueOf(roomId));
        session.send(sendHeaders, messageToSend);

        // Then
        ChatSendMessage receivedMessage = future.get(5, TimeUnit.SECONDS);

        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getContent()).isEqualTo(messageContent);
        assertThat(receivedMessage.getSender()).isEqualTo(userName);
    }
}
