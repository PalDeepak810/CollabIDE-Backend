package com.CollabIDE.filestate;

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
public class FileState {
    private UUID sessionId;
    private  String filePath;
    private String content;
    private long version;
    private Instant lastModifiedAt;
    private String lastModifiedBy;
}
