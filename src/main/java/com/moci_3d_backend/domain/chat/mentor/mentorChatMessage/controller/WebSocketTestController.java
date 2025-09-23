package com.moci_3d_backend.domain.chat.mentor.mentorChatMessage.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile(value = "test")
@Controller
@RequestMapping("/api/v1/test/websocket")
public class WebSocketTestController {

    @GetMapping
    public String websocketTestPage() {
        return "websocket-test";
    }
}
