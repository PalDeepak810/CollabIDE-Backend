package com.CollabIDE.Repositories;

import com.CollabIDE.Entities.FileStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileStateRepository extends JpaRepository<FileStateEntity, UUID> {
    Optional<FileStateEntity> findBySessionIdAndFilePath(
            UUID sessionId, String filePath
    );

    List<FileStateEntity> findBySessionId(UUID sessionId);
}
