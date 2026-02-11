package com.CollabIDE.Repositories;


import com.CollabIDE.Entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {
}
