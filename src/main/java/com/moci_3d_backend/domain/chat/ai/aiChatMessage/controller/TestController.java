package com.moci_3d_backend.domain.chat.ai.aiChatMessage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class TestController {
    @GetMapping("/room")
    public String showRoom() {
        return "chat/room";
    }
}
