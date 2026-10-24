package com.troy.ats.repository;

import com.troy.ats.entity.Client;
import com.troy.ats.entity.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StatusRepository extends JpaRepository<Status, UUID> {

    List<Status> findByShowInPipelineTrue();

    @EntityGraph(attributePaths = {"subStatuses"})
    Optional<Status> findByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"subStatuses"})
    List<Status> findByActiveTrue();

    @Override
    @EntityGraph(attributePaths = {"subStatuses"})
    Optional<Status> findById(UUID id);
}