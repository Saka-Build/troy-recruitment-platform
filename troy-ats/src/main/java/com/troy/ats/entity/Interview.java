package com.troy.ats.entity;

import com.troy.ats.enums.InterviewOutcome;
import com.troy.ats.enums.InterviewRound;
import com.troy.ats.enums.InterviewType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "interviews",
        indexes = {
                @Index(name = "idx_interview_submission", columnList = "submission_id"),
                @Index(name = "idx_interview_candidate", columnList = "candidate_id"),
                @Index(name = "idx_interview_job", columnList = "job_id"),
                @Index(name = "idx_interview_date", columnList = "interview_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // Submission
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "submission_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_interview_submission")
    )
    private Submission submission;

    // Candidate
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "candidate_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_interview_candidate")
    )
    private Candidate candidate;

    // Job
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "job_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_interview_job")
    )
    private Job job;

    @Column(name = "interview_date", nullable = false)
    private LocalDate interviewDate;

    @Column(name = "interview_time")
    private LocalTime interviewTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type")
    private InterviewType interviewType;

    @Enumerated(EnumType.STRING)
    @Column(name = "round")
    private InterviewRound round;

    @Column(name = "interviewer_name", length = 255)
    private String interviewerName;

    @Column(name = "interviewer_email", length = 255)
    private String interviewerEmail;

    @Column(name = "meeting_link", columnDefinition = "text")
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "outcome", length = 20)
    private InterviewOutcome outcome;

    @Column(name = "feedback", columnDefinition = "text")
    private String feedback;

    // Employee who scheduled the interview
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "scheduled_by",
            foreignKey = @ForeignKey(name = "fk_interview_scheduled_by")
    )
    private Employee scheduledBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (outcome == null) {
            outcome = InterviewOutcome.scheduled;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}