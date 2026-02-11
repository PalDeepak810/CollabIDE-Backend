package com.CollabIDE.Entities;

import com.CollabIDE.filestate.FileState;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "file_states",
        uniqueConstraints = @UniqueConstraint(columnNames = {"session_id", "file_path"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileStateEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(name = "file_path", nullable = false, length = 1024)
    private String filePath;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private long version;

    @Column(name = "last_modified_at", nullable = false)
    private Instant lastModifiedAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

}
