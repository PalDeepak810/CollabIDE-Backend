package com.CollabIDE.websocket.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class FileUpdateMessage {
    private UUID sessionId;
    private String filePath;
    private String content;
    private long version;
    private String userId;
}
