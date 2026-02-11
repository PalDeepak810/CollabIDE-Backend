package com.CollabIDE.Mapper;

import com.CollabIDE.Entities.FileStateEntity;
import com.CollabIDE.filestate.FileState;

public class FileStateMapper {
    public static FileState toDomain(FileStateEntity entity) {
        return FileState.builder()
                .sessionId(entity.getSessionId())
                .filePath(entity.getFilePath())
                .content(entity.getContent())
                .version(entity.getVersion())
                .lastModifiedAt(entity.getLastModifiedAt())
                .lastModifiedBy(entity.getLastModifiedBy())
                .build();
    }

    public static FileStateEntity toEntity(FileState domain) {
        return FileStateEntity.builder()
                .sessionId(domain.getSessionId())
                .filePath(domain.getFilePath())
                .content(domain.getContent())
                .version(domain.getVersion())
                .lastModifiedAt(domain.getLastModifiedAt())
                .lastModifiedBy(domain.getLastModifiedBy())
                .build();
    }
}
