package com.CollabIDE.websocket.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class EditOperationMessage {
    private UUID sessionId;
    private String filePath;
    private String userId;

    private long baseVersion;

    private OperationType type;

    private Integer position;
    private Integer length;
    private String text;

    private Instant timestamp;

    public enum OperationType{
        INSERT,
        DELETE
    }
}
