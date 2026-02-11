package com.CollabIDE.websocket.controller;

import com.CollabIDE.websocket.dto.ActivityMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ActivityWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public ActivityWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/activity")
    public void handleActivity(ActivityMessage message) {
        if (message == null || message.getSessionId() == null) return;
        message.setType("ACTIVITY");
        messagingTemplate.convertAndSend("/topic/session/" + message.getSessionId(), message);
    }
}
