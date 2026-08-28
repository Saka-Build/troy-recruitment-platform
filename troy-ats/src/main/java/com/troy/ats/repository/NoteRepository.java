package com.troy.ats.repository;

import com.troy.ats.entity.ActivityLog;
import com.troy.ats.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID>, JpaSpecificationExecutor<Note> {

    @EntityGraph(attributePaths = {"createdBy"})
    List<Note> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, UUID entityId);
}