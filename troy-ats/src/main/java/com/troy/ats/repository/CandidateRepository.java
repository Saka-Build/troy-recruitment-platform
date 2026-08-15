package com.troy.ats.repository;

import com.troy.ats.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID>, JpaSpecificationExecutor<Candidate> {

    long countByStatus_Active(boolean active);
    long countByStatus_Name(String statusName);
    List<Candidate> findByStatus_NameAndSubStatus_Name(String statusName, String subStatusName);
}