package com.CollabIDE.session;

import com.CollabIDE.session.Session;
import com.CollabIDE.session.SessionParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SessionState {
    private Session session;
    private List<SessionParticipant> participants;
}
