package com.CollabIDE.websocket.controller;

import com.CollabIDE.websocket.dto.CursorMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class CursorWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public CursorWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/cursor")
    public void handleCursor(CursorMessage message) {
        if (message == null || message.getSessionId() == null) return;
        message.setType("CURSOR");

        messagingTemplate.convertAndSend(
                "/topic/session/" + message.getSessionId(),
                message
        );
    }
}
