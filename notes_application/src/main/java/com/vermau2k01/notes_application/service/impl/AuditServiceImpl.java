package com.vermau2k01.notes_application.service.impl;

import com.vermau2k01.notes_application.entity.AuditLog;
import com.vermau2k01.notes_application.entity.Notes;
import com.vermau2k01.notes_application.repository.AuditRepository;
import com.vermau2k01.notes_application.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;


    @Override
    public void logNoteCreation(String username, Notes note) {
        AuditLog log = new AuditLog();
        log.setAction("CREATE");
        log.setUsername(username);
        log.setNoteId(note.getNoteId());
        log.setNoteContent(note.getContent());
        log.setTimeStamp(LocalDateTime.now());

        auditRepository.save(log);
    }

    @Override
    public void logNoteUpdate(String username, Notes note) {

        AuditLog log = new AuditLog();
        log.setAction("UPDATE");
        log.setUsername(username);
        log.setNoteId(note.getNoteId());
        log.setNoteContent(note.getContent());
        log.setTimeStamp(LocalDateTime.now());

        auditRepository.save(log);

    }

    @Override
    public void logNoteDeletion(String username, String noteId) {

        AuditLog log = new AuditLog();
        log.setAction("DELETE");
        log.setUsername(username);
        log.setNoteId(noteId);
        log.setTimeStamp(LocalDateTime.now());

        auditRepository.save(log);
    }

    @Override
    public List<AuditLog> getAllLogs() {
        return auditRepository.findAll();
    }

    @Override
    public List<AuditLog> getNoteAuditLog(String id) {
        return auditRepository.findByNoteId(id);
    }
}
