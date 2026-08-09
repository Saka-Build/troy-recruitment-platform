package com.troy.ats.repository;

import com.troy.ats.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    List<Interview> findByCandidateId(UUID candidateId);
    List<Interview> findByJobId(UUID jobId);
    List<Interview> findByInterviewDate(LocalDate date);
}

