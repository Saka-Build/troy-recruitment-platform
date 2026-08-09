package com.troy.ats.repository;

import com.troy.ats.entity.AiReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AiReviewRepository extends JpaRepository<AiReview, UUID> {
    Optional<AiReview> findByCandidateIdAndJobId(UUID candidateId, UUID jobId);
    List<AiReview> findByCandidateId(UUID candidateId);
    List<AiReview> findByJobId(UUID jobId);
}

