package com.troy.ats.repository;

import com.troy.ats.entity.Interview;
import com.troy.ats.enums.InterviewOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    List<Interview> findByCandidateId(UUID candidateId);
    List<Interview> findByJobId(UUID jobId);
    List<Interview> findByInterviewDate(LocalDate date);
    List<Interview> findByInterviewDateTimeWithZoneGreaterThanEqualAndInterviewDateTimeWithZoneLessThanOrderByInterviewDateTimeWithZoneDesc(Instant start, Instant end);

    // Count interviews with a particular outcome and no feedback
    @Query("""
        SELECT COUNT(i) FROM Interview i WHERE i.outcome = :outcome AND (i.feedback IS NULL OR TRIM(i.feedback) = '')
        """)
    long countByOutcomeAndFeedbackEmpty(@Param("outcome") InterviewOutcome outcome);

    List<Interview> findBySubmissionId(UUID submissionId);

}