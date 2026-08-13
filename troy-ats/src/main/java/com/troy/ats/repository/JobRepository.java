package com.troy.ats.repository;

import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    long countByStatus(JobStatus status);
    long countByPriority(String priority);
}