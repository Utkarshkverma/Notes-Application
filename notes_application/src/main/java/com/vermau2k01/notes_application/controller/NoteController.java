package com.vermau2k01.notes_application.controller;

import com.vermau2k01.notes_application.entity.Notes;
import com.vermau2k01.notes_application.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @PostMapping
    public Notes createNote(@RequestBody String content,
                            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        logger.info("Creating new note for user: {}", username);
        return noteService.createNoteForUser(username, content);
    }

    @GetMapping
    public List<Notes> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        logger.info("Fetching all notes for user: {}", username);
        return noteService.getNotesForUser(username);
    }

    @PutMapping("/{noteId}")
    public Notes updateNote(@PathVariable String noteId,
                            @RequestBody String content,
                            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return noteService.updateNoteForUser(noteId,content, username);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable String noteId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        noteService.deleteNoteForUser(noteId, username);
    }


}
