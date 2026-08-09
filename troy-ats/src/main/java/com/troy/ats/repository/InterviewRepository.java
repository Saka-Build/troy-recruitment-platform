package com.troy.ats.repository;

import com.troy.ats.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByCandidateId(Long candidateId);
    List<Interview> findByJobId(Long jobId);
    List<Interview> findByInterviewDate(LocalDate date);
}