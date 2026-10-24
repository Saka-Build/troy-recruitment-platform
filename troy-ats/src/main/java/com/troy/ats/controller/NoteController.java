package com.troy.ats.controller;

import com.troy.ats.dto.NoteDto;
import com.troy.ats.dto.NoteRequest;
import com.troy.ats.entity.Note;
import com.troy.ats.service.impl.NoteServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteServiceImpl noteService;

    public NoteController(NoteServiceImpl noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{entityType}/{entityId}")
    public ResponseEntity<List<NoteDto>> getNoteLogs(
                                                 @PathVariable String entityType,
                                                 @PathVariable UUID entityId) {

        return ResponseEntity.ok(noteService.getNotesForEntityTypeAndId(entityType, entityId));
    }

    @PostMapping("/create")
    public ResponseEntity<NoteDto> createNote(
                                    @Valid @RequestBody NoteRequest request) {

        NoteDto response = noteService.createNote(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}