package com.CollabIDE.websocket.dto;

import lombok.Data;

@Data
public class FileOperationRequest {
    private String path;
    private String name;
    private String content;
    private String userId;
}
