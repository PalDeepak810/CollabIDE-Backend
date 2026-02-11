package com.CollabIDE.Services;

import com.CollabIDE.Mapper.EditOperationMapper;
import com.CollabIDE.Mapper.FileStateMapper;
import com.CollabIDE.Repositories.EditOperationRepository;
import com.CollabIDE.Repositories.FileStateRepository;
import com.CollabIDE.filestate.FileState;
import com.CollabIDE.operation.DeleteOperation;
import com.CollabIDE.operation.EditOperation;
import com.CollabIDE.operation.OTEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class FileStateService {

        private final FileStateRepository fileStateRepository;
        private final EditOperationRepository editOperationRepository;
        private final OTEngine otEngine;

        public FileStateService(
                        FileStateRepository fileStateRepository,
                        EditOperationRepository editOperationRepository,
                        OTEngine otEngine) {
                this.fileStateRepository = fileStateRepository;
                this.editOperationRepository = editOperationRepository;
                this.otEngine = otEngine;
        }

        // CORE OT METHOD (DELETE-SAFE, ADVANCED-READY)

        @Transactional
        public FileState applyOperation(EditOperation incoming) {

                // 1️. Load managed entity
                var entity = fileStateRepository
                                .findBySessionIdAndFilePath(
                                                incoming.getSessionId(),
                                                incoming.getFilePath())
                                .orElseThrow(() -> new IllegalArgumentException("File not found"));

                // 2️. Load concurrent ops AFTER client's baseVersion
                List<EditOperation> concurrentOps = editOperationRepository
                                .findBySessionIdAndFilePathAndBaseVersionGreaterThan(
                                                incoming.getSessionId(),
                                                incoming.getFilePath(),
                                                incoming.getBaseVersion())
                                .stream()
                                .map(EditOperationMapper::toDomain)
                                .toList();

                // 3️. Rebase incoming op using OT
                EditOperation rebased = incoming;
                for (EditOperation concurrent : concurrentOps) {
                        rebased = otEngine.transform(rebased, concurrent);
                }

                // IMPORTANT: ignore empty DELETE after transform
                if (rebased instanceof DeleteOperation del) {
                        if (del.getLength() <= 0) {
                                return FileStateMapper.toDomain(entity);
                        }
                }

                // 4️. Apply rebased op safely
                String newContent = otEngine.apply(
                                entity.getContent(),
                                rebased);

                if (newContent == null) {
                        throw new IllegalStateException("OT produced null content");
                }

                // 5️. Persist rebased operation
                editOperationRepository.save(
                                EditOperationMapper.toEntity(rebased));

                // 6️. Mutate SAME managed entity (UPDATE)
                entity.setContent(newContent);
                entity.setVersion(entity.getVersion() + 1);
                entity.setLastModifiedAt(Instant.now());
                entity.setLastModifiedBy(rebased.getUserId());

                fileStateRepository.save(entity);

                // 7️. Return domain object
                return FileStateMapper.toDomain(entity);
        }

        // FILE CREATION

        @Transactional
        public FileState createFile(
                        UUID sessionId,
                        String filePath,
                        String initialContent,
                        String userId) {

                boolean exists = fileStateRepository
                                .findBySessionIdAndFilePath(sessionId, filePath)
                                .isPresent();

                if (exists) {
                        throw new IllegalStateException("File already exists");
                }

                FileState file = FileState.builder()
                                .sessionId(sessionId)
                                .filePath(filePath)
                                .content(initialContent)
                                .version(1)
                                .lastModifiedAt(Instant.now())
                                .lastModifiedBy(userId)
                                .build();

                fileStateRepository.save(
                                FileStateMapper.toEntity(file));

                return file;
        }

        // READ METHODS
        public Collection<FileState> listFiles(UUID sessionId) {
                return fileStateRepository
                                .findBySessionId(sessionId)
                                .stream()
                                .map(FileStateMapper::toDomain)
                                .toList();
        }

        // NON-OT UPDATE
        @Transactional
        public FileState updateFileContent(
                        UUID sessionId,
                        String filePath,
                        String newContent,
                        String userId) {

                if (newContent == null) {
                        throw new IllegalArgumentException("Content cannot be null");
                }

                var entity = fileStateRepository
                                .findBySessionIdAndFilePath(sessionId, filePath)
                                .orElseThrow(() -> new IllegalArgumentException("File not found"));

                entity.setContent(newContent);
                entity.setVersion(entity.getVersion() + 1);
                entity.setLastModifiedAt(Instant.now());
                entity.setLastModifiedBy(userId);

                fileStateRepository.save(entity);
                return FileStateMapper.toDomain(entity);
        }
}
