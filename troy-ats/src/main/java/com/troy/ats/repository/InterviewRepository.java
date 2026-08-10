package com.troy.ats.repository;

import com.troy.ats.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByCandidateId(UUID candidateId);
    List<Interview> findByJobId(UUID jobId);
    List<Interview> findByInterviewDate(LocalDate date);
}