package com.troy.ats.repository;

import com.troy.ats.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    Optional<Candidate> findByCvId(String cvId);
    List<Candidate> findByStatusId(UUID statusId);
    List<Candidate> findByCvOwnerId(UUID cvOwnerId);
    List<Candidate> findByLocation(String location);
}

