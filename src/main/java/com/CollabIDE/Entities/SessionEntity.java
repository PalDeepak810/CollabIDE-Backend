package com.CollabIDE.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionEntity {
    @Id
    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "owner_user_id", nullable = false)
    private String ownerUserId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
