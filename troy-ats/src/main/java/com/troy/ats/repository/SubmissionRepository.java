package com.troy.ats.repository;

import com.troy.ats.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    Optional<Submission> findByCandidateIdAndJobId(UUID candidateId, UUID jobId);
    List<Submission> findByCandidateId(UUID candidateId);
    List<Submission> findByJobId(UUID jobId);
    List<Submission> findByPipelineStage(com.troy.ats.enums.PipelineStage stage);
}

