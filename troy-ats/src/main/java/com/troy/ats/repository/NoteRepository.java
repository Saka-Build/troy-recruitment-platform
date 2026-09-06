package com.troy.ats.repository;

import com.troy.ats.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID>, JpaSpecificationExecutor<Note> {

    @EntityGraph(attributePaths = {"createdBy"})
    List<Note> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(String entityType, UUID entityId);

    @Query("""
    SELECT n
    FROM Note n
    WHERE n.entityType = :entityType
      AND n.entityId IN :entityIds
      AND n.createdAt = (
          SELECT MAX(n2.createdAt)
          FROM Note n2
          WHERE n2.entityType = n.entityType
            AND n2.entityId = n.entityId
      )
    """)
    List<Note> findLatestNotes(
            @Param("entityType") String entityType,
            @Param("entityIds") List<UUID> entityIds
    );
}