package com.troy.ats.repository;

import com.troy.ats.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID>, JpaSpecificationExecutor<Candidate> {

    @Query("""
    SELECT c.status.id, COUNT(c)
    FROM Candidate c
    WHERE c.status IS NOT NULL
    GROUP BY c.status.id
    """)

    List<Object[]> countCandidatesByStatus();
    long countByActive(boolean active);
    long countByStatus_Name(String statusName);
    List<Candidate> findByStatus_NameAndSubStatus_Name(String statusName, String subStatusName);
}