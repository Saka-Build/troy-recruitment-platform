package com.troy.ats.repository;

import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {

    long countByPipelineStage(PipelineStage pipelineStage);
}