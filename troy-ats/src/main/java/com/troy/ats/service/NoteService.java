package com.troy.ats.service;


import com.troy.ats.dto.NoteDto;
import com.troy.ats.dto.NoteRequest;

import java.util.List;
import java.util.UUID;

public interface NoteService {

   NoteDto createNote(NoteRequest request);
   List<NoteDto> getNotesForEntityTypeAndId(String entityType, UUID entityId);


}