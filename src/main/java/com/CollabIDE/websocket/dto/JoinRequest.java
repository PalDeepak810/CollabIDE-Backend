package com.CollabIDE.websocket.dto;

import lombok.Data;

@Data
public class JoinRequest {
    private String userId;
    private String name;
    private String tabId;
}
