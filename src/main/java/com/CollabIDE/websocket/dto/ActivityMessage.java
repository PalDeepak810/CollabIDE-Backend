package com.CollabIDE.websocket.dto;

import lombok.Data;

@Data
public class ActivityMessage {
    private String type;
    private String sessionId;
    private String userId;
    private String name;
    private String status;
    private String timestamp;
}
