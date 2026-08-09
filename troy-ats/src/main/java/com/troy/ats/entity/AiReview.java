package com.troy.ats.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.troy.ats.enums.AiRecommendation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * AI Review entity for Troy ATS
 */
@Entity
@Table(name = "ai_reviews", indexes = {
    @Index(name = "idx_ai_reviews_candidate_id", columnList = "candidate_id"),
    @Index(name = "idx_ai_reviews_job_id", columnList = "job_id"),
    @Index(name = "idx_ai_reviews_score", columnList = "match_score DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "match_score", precision = 5, scale = 2)
    private Double matchScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation")
    private AiRecommendation recommendation;

    @Column(name = "skills_match", columnDefinition = "JSONB")
    private JsonNode skillsMatch;

    @Column(name = "experience_fit", columnDefinition = "JSONB")
    private JsonNode experienceFit;

    @Column(name = "education_fit", columnDefinition = "JSONB")
    private JsonNode educationFit;

    @Column(name = "certifications_fit", columnDefinition = "JSONB")
    private JsonNode certificationsFit;

    @Column(name = "visa_check", columnDefinition = "JSONB")
    private JsonNode visaCheck;

    @Column(name = "location_check", columnDefinition = "JSONB")
    private JsonNode locationCheck;

    @Column(name = "notice_check", columnDefinition = "JSONB")
    private JsonNode noticeCheck;

    @Column(name = "salary_check", columnDefinition = "JSONB")
    private JsonNode salaryCheck;

    @Column(name = "submission_readiness", precision = 5, scale = 2)
    private Double submissionReadiness;

    @Column(name = "recruiter_summary", columnDefinition = "TEXT")
    private String recruiterSummary;

    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths;

    @Column(name = "weaknesses", columnDefinition = "TEXT")
    private String weaknesses;

    @Column(name = "risks", columnDefinition = "TEXT")
    private String risks;

    @Column(name = "interview_questions", columnDefinition = "JSONB")
    private JsonNode interviewQuestions;

    @Column(name = "full_report_json", columnDefinition = "JSONB")
    private JsonNode fullReportJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Employee reviewedBy;

    @Column(name = "ai_model_used", length = 100)
    private String aiModelUsed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
