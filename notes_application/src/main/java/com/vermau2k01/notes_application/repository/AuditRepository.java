package com.vermau2k01.notes_application.repository;

import com.vermau2k01.notes_application.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<AuditLog,String> {

    List<AuditLog> findByNoteId(String noteId);
}
