package com.troy.ats.service.impl;

import com.troy.ats.dto.NoteDto;
import com.troy.ats.dto.NoteRequest;
import com.troy.ats.entity.Note;
import com.troy.ats.populator.NotePopulator;
import com.troy.ats.repository.NoteRepository;
import com.troy.ats.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service("noteService")
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {


    private final SessionServiceImpl sessionService;
    private final NoteRepository noteRepository;
    private final NotePopulator notePopulator;

    @Override
    public NoteDto createNote(NoteRequest request) {
        Note note = new Note();
        populateNote(request, note);
        note = noteRepository.save(note);
        NoteDto noteDto = new NoteDto();
        notePopulator.populate(note, noteDto, sessionService);

        return noteDto;
    }

    @Override
    public List<NoteDto> getNotesForEntityTypeAndId(String entityType, UUID entityId) {

        List<Note> notes = noteRepository.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType,entityId);
        List<NoteDto> noteDtoList = notes.stream().map(note -> {
                                        NoteDto noteDto = new NoteDto();
                                        notePopulator.populate(note, noteDto, sessionService);
                                        return noteDto;
                                    }).toList();
        return noteDtoList;
    }

    private void populateNote(NoteRequest request, Note note) {

        note.setEntityType(request.getEntityType().toLowerCase(Locale.ROOT));
        note.setEntityId(request.getEntityId());
        note.setContent(request.getContent());
        note.setPinned(request.getPinned());
        note.setCreatedBy(sessionService.getCurrentUser());
        note.setCreatedAt(Instant.now());
    }
}