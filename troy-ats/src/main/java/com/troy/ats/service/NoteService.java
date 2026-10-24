package com.troy.ats.service;


import com.troy.ats.dto.NoteDto;
import com.troy.ats.dto.NoteRequest;
import com.troy.ats.entity.Note;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NoteService {

   NoteDto createNote(NoteRequest request);
   List<NoteDto> getNotesForEntityTypeAndId(String entityType, UUID entityId);

   /**
    *
    * @param entityType
    * @param entityIds
    * @return
    */
   List<Note> findLatestNotes(String entityType, List<UUID> entityIds);


}