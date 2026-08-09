package com.troy.ats.repository;

import com.troy.ats.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    Optional<Offer> findBySubmissionId(UUID submissionId);
    List<Offer> findByCandidateId(UUID candidateId);
    List<Offer> findByJobId(UUID jobId);
}

