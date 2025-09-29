package com.moci_3d_backend.domain.webRTC.controller;

import com.moci_3d_backend.domain.webRTC.dto.WebRTCDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebRTCController {

    @MessageMapping("/webrtc")
    @SendTo("/topic/webrtc")
    public WebRTCDto signaling(WebRTCDto message) {
        // 메시지를 그대로 반환하여 구독자에게 전달
        return message;
    }
}
