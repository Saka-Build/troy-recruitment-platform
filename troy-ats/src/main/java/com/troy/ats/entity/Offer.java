package com.troy.ats.entity;

import com.troy.ats.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "offers",
        indexes = {
                @Index(name = "idx_offer_candidate", columnList = "candidate_id"),
                @Index(name = "idx_offer_job", columnList = "job_id"),
                @Index(name = "idx_offer_status", columnList = "offer_status"),
                @Index(name = "idx_offer_created_by", columnList = "created_by")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /*
     * One submission can have only one offer
     * because submission_id is UNIQUE in database.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "submission_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_offer_submission")
    )
    private Submission submission;

    /*
     * Candidate receiving the offer
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "candidate_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_offer_candidate")
    )
    private Candidate candidate;

    /*
     * Job for which offer is being made
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "job_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_offer_job")
    )
    private Job job;

    @Column(
            name = "offered_salary",
            precision = 14,
            scale = 2
    )
    private BigDecimal offeredSalary;

    @Column(name = "salary_currency", length = 3)
    private String salaryCurrency;

    @Column(name = "joining_date")
    private Instant joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "offer_status",
            nullable = false
    )
    private OfferStatus offerStatus;

    @Column(name = "offer_letter_url", columnDefinition = "text")
    private String offerLetterUrl;

    @Column(name = "released_at")
    private OffsetDateTime releasedAt;

    @Column(name = "accepted_at")
    private OffsetDateTime acceptedAt;

    @Column(name = "declined_at")
    private OffsetDateTime declinedAt;

    @Column(name = "decline_reason", columnDefinition = "text")
    private String declineReason;

    @Column(name = "notes", columnDefinition = "text")
    private String notes;

    /*
     * Employee who created the offer
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            foreignKey = @ForeignKey(name = "fk_offer_created_by")
    )
    private Employee createdBy;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        OffsetDateTime now = OffsetDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (salaryCurrency == null) {
            salaryCurrency = "USD";
        }

        if (offerStatus == null) {
            offerStatus = OfferStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}