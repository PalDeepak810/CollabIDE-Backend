package com.CollabIDE.websocket.dto;

import lombok.Data;

@Data
public class CursorMessage {
    private String type;
    private String sessionId;
    private String filePath;
    private String userId;
    private String name;
    private Integer line;
    private Integer column;
}
