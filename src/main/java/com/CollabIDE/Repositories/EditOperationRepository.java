package com.CollabIDE.Repositories;

import com.CollabIDE.Entities.EditOperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EditOperationRepository extends JpaRepository<EditOperationEntity,Long> {

    List<EditOperationEntity> findBySessionIdAndFilePathOrderByIdAsc(
            UUID sessionId,
            String filePath
    );

    List<EditOperationEntity> findBySessionIdAndFilePathAndBaseVersionGreaterThanEqual(
      UUID sessionId,
      String filePath,
      long baseVersion
    );

    List<EditOperationEntity>
    findBySessionIdAndFilePathAndBaseVersionGreaterThan(
            UUID sessionId,
            String filePath,
            long baseVersion
    );

}
