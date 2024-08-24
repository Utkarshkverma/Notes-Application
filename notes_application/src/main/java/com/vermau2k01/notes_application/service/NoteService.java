package com.vermau2k01.notes_application.service;

import com.vermau2k01.notes_application.entity.Notes;

import java.util.List;

public interface NoteService {

    Notes createNoteForUser(String username, String content);

    Notes updateNoteForUser(String noteId, String content, String username);

    void deleteNoteForUser(String noteId, String username);

    List<Notes> getNotesForUser(String username);

}
