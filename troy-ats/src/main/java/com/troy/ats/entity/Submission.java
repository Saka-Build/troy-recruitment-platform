package com.troy.ats.entity;

import com.troy.ats.enums.Currency;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.enums.RatePeriod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {
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

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "pipeline_stage", nullable = false)
    private PipelineStage pipelineStage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_status_id")
    private SubStatus subStatus;

    @Column(name = "candidate_expected_amount", precision = 12, scale = 2)
    private BigDecimal candidateExpectedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_expected_currency", length = 3)
    private Currency candidateExpectedCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_expected_period", length = 20)
    private RatePeriod candidateExpectedPeriod;

    @Column(name = "submission_amount", precision = 12, scale = 2)
    private BigDecimal submissionAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "submission_currency", length = 3)
    private Currency submissionCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "submission_period", length = 20)
    private RatePeriod submissionPeriod;

    @Column(name = "offer_amount", precision = 12, scale = 2)
    private BigDecimal offerAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "offer_currency", length = 3)
    private Currency offerCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "offer_period", length = 20)
    private RatePeriod offerPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private Employee submittedBy;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

