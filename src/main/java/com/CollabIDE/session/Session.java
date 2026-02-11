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
public class Session {
    private UUID sessionId;

    private  String ownerUserId;

    private Instant createdAt;

}
