package com.CollabIDE.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "session_participants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "user_id", "tab_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionParticipantEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "tab_id")
    private String tabId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Column(name = "last_active_at")
    private Instant lastActiveAt;

    public enum Role {
        OWNER,
        COLLABORATOR
    }
}
