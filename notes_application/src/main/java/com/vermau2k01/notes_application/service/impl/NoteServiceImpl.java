package com.vermau2k01.notes_application.service.impl;

import com.vermau2k01.notes_application.entity.Notes;
import com.vermau2k01.notes_application.repository.NotesRepository;
import com.vermau2k01.notes_application.service.AuditService;
import com.vermau2k01.notes_application.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NotesRepository notesRepository;
    private final AuditService auditService;

    @Override
    public Notes createNoteForUser(String username, String content) {
        Notes build = Notes
                .builder()
                .ownerUserName(username)
                .content(content)
                .build();

        auditService.logNoteCreation(username, build);

        return notesRepository.save(build);
    }

    @Override
    public Notes updateNoteForUser(String noteId, String content, String username) {
        Notes note = notesRepository.findById(noteId).orElseThrow(()
                -> new RuntimeException("Note not found"));
        note.setContent(content);
        auditService.logNoteUpdate(username, note);
        return notesRepository.save(note);
    }

    @Override
    public void deleteNoteForUser(String noteId, String username) {
        auditService.logNoteDeletion(username, noteId);
        notesRepository.deleteById(noteId);
    }

    @Override
    public List<Notes> getNotesForUser(String username) {
        return notesRepository
                .findByOwnerUserName(username);
    }
}
