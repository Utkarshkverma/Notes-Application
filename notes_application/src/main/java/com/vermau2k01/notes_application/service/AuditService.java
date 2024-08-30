package com.vermau2k01.notes_application.service;

import com.vermau2k01.notes_application.entity.AuditLog;
import com.vermau2k01.notes_application.entity.Notes;

import java.util.List;

public interface AuditService {

    void logNoteCreation(String username,Notes note);
    void logNoteUpdate(String username,Notes note);
    void logNoteDeletion(String username,String noteId);

    List<AuditLog> getAllLogs();

    List<AuditLog> getNoteAuditLog(String id);
}
