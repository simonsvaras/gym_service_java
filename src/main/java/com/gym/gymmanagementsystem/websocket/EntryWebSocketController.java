package com.gym.gymmanagementsystem.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EntryWebSocketController {

    @MessageMapping("/entry")
    @SendTo("/topic/entries")
    public String handleEntry(String message) {
        return message;
    }
}
