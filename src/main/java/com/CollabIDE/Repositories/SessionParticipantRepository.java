package com.CollabIDE.Repositories;

import com.CollabIDE.Entities.SessionParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SessionParticipantRepository extends JpaRepository<SessionParticipantEntity, UUID> {
    List<SessionParticipantEntity> findBySessionId(UUID sessionId);
}
