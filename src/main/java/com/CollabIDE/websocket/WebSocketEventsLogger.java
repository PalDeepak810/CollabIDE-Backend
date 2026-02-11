package com.CollabIDE.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventsLogger {

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        System.out.println("✅ STOMP CONNECTED");
    }

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        System.out.println("📡 STOMP SUBSCRIBED");
    }
}
