package com.CollabIDE.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionParticipant {
    private UUID sessionId;
    private String userID;
    private String tabId;
    private  Role role;
    private Instant joinedAt;
    private Instant lastActiveAT;

    public enum Role{
        OWNER,
        COLLABORATOR
    }

}
