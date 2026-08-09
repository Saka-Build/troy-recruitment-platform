package com.troy.ats.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.troy.ats.enums.PipelineStage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Submission entity for Troy ATS
 */
@Entity
@Table(name = "submissions", uniqueConstraints = {
    @UniqueConstraint(name = "uk_submissions_candidate_job", columnNames = {"candidate_id", "job_id"})
}, indexes = {
    @Index(name = "idx_submissions_candidate_id", columnList = "candidate_id"),
    @Index(name = "idx_submissions_job_id", columnList = "job_id"),
    @Index(name = "idx_submissions_stage", columnList = "pipeline_stage")
})
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
    @Column(name = "pipeline_stage", nullable = false)
    private PipelineStage pipelineStage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_status_id")
    private SubStatus subStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private Employee submittedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "submission", fetch = FetchType.LAZY)
    @JsonBackReference
    private Offer offer;

    @OneToMany(mappedBy = "submission", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Interview> interviews = new HashSet<>();

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
