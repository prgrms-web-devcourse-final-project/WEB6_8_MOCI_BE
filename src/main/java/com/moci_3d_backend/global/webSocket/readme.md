# WebSocket

## WebSocketConfig

여기에서 Websocket이 어느 경로로 접속해서
어느경로로 메시지를 받을 것이고
어느경로로 메시지를 전송할 것인지를 설정합니다.

## security/JwtHandshakeInterceptor

위에서 설정한 경로로 접속을 했을 경우의 동작을 설정합니다. 

## security/StompJwtInterceptor

위에서 설정한 메시지를 받았을 때의 동작을 설정합니다.
