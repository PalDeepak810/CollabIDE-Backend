package com.CollabIDE.websocket.controller;

import com.CollabIDE.Services.FileStateService;
import com.CollabIDE.Services.SessionService;
import com.CollabIDE.filestate.FileState;
import com.CollabIDE.session.SessionParticipant;
import com.CollabIDE.session.SessionState;
import com.CollabIDE.websocket.dto.FileOperationRequest;
import com.CollabIDE.websocket.dto.JoinRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class SessionWebSocketController {

    private final SessionService sessionService;
    private final FileStateService fileStateService;
    private final SimpMessagingTemplate messagingTemplate;

    public SessionWebSocketController(SessionService sessionService, FileStateService fileStateService, SimpMessagingTemplate messagingTemplate) {
        this.sessionService = sessionService;
        this.fileStateService = fileStateService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/session/{sessionId}/join")
    public void handleJoin(@DestinationVariable UUID sessionId, JoinRequest request) {
        String tabId = request.getTabId();
        if (tabId == null || tabId.isBlank()) {
            tabId = UUID.randomUUID().toString();
        }
        // 1. Join session in DB
        SessionState state = sessionService.joinSession(sessionId, request.getUserId(), tabId);

        // 2. Prepare broadcast message for current users (PRESENCE)
        Map<String, Object> presenceMsg = new HashMap<>();
        presenceMsg.put("type", "PRESENCE");
        presenceMsg.put("users", state.getParticipants().stream().map(p -> {
            Map<String, Object> u = new HashMap<>();
            u.put("userId", p.getUserID());
            u.put("tabId", p.getTabId());
            u.put("name", p.getUserID()); // Using userID as name for now as JoinRequest doesn't have name sometimes
            return u;
        }).collect(Collectors.toList()));

        messagingTemplate.convertAndSend("/topic/session/" + sessionId, presenceMsg);

        // 3. Send full session state to the joining user (SESSION_STATE)
        Map<String, Object> stateMsg = new HashMap<>();
        stateMsg.put("type", "SESSION_STATE");
        stateMsg.put("files", fileStateService.listFiles(sessionId));
        stateMsg.put("users", presenceMsg.get("users"));
        stateMsg.put("ownerId", state.getSession().getOwnerUserId());

        // In a real STOMP setup, we might use @SendToUser or similar, 
        // but for simplicity let's broadcast or assume client handles it.
        // Actually, broadcasting state is fine for small groups.
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, stateMsg);
    }

    @MessageMapping("/session/{sessionId}/files/create")
    public void handleFileCreate(@DestinationVariable UUID sessionId, FileOperationRequest request) {
        if (request.getUserId() == null || !sessionService.isParticipant(sessionId, request.getUserId())) {
            Map<String, Object> err = new HashMap<>();
            err.put("type", "ERROR");
            err.put("payload", "Not a session participant");
            messagingTemplate.convertAndSend("/topic/session/" + sessionId, err);
            return;
        }
        FileState file = fileStateService.createFile(sessionId, request.getPath(), request.getContent(), "system");
        
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "FILE_CREATED");
        msg.put("file", file);
        
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, msg);
    }

    // Add other file operations as needed...
}
