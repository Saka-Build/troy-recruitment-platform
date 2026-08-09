package com.troy.ats.repository;

import com.troy.ats.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByClientId(UUID clientId);
    List<Job> findByStatus(com.troy.ats.enums.JobStatus status);
    List<Job> findByOwnerId(UUID ownerId);
}

