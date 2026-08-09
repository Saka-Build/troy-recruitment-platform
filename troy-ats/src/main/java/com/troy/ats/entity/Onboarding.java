package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "onboarding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Onboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "offer_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_onboarding_offer")
    )
    private Offer offer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "candidate_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_onboarding_candidate")
    )
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "job_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_onboarding_job")
    )
    private Job job;

    @Column(name = "joining_confirmed_at")
    private OffsetDateTime joiningConfirmedAt;

    @Column(name = "security_clearance_at")
    private OffsetDateTime securityClearanceAt;

    @Column(name = "onboarding_started_at")
    private OffsetDateTime onboardingStartedAt;

    /**
     * When this is set, the candidate is considered placed/onboarded.
     */
    @Column(name = "onboarded_at")
    private OffsetDateTime onboardedAt;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "managed_by",
            foreignKey = @ForeignKey(name = "fk_onboarding_managed_by")
    )
    private Employee managedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
