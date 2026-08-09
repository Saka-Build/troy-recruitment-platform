package com.troy.ats.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.JobType;
import com.troy.ats.enums.JobWorkMode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Job/Role entity for Troy ATS
 */
@Entity
@Table(name = "jobs", indexes = {
    @Index(name = "idx_jobs_client_id", columnList = "client_id"),
    @Index(name = "idx_jobs_status", columnList = "status"),
    @Index(name = "idx_jobs_priority", columnList = "priority"),
    @Index(name = "idx_jobs_skills_gin", columnList = "skills_required", columnDefinition = "text[]"),
    @Index(name = "idx_jobs_created_at", columnList = "created_at DESC"),
    @Index(name = "idx_jobs_is_template", columnList = "is_template")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "country", length = 100)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode")
    private JobWorkMode workMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private JobType jobType;

    @Column(name = "industry", length = 100)
    private String industry;

    @Column(name = "experience_min", precision = 4)
    private Double experienceMin;

    @Column(name = "experience_max", precision = 4)
    private Double experienceMax;

    @Column(name = "salary_min", precision = 14, scale = 2)
    private Double salaryMin;

    @Column(name = "salary_max", precision = 14, scale = 2)
    private Double salaryMax;

    @Column(name = "salary_currency", length = 3)
    private String salaryCurrency;

    @ElementCollection
    @CollectionTable(name = "job_skills_required", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill", length = 100)
    private Set<String> skillsRequired = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status;

    @Column(name = "priority", nullable = false, length = 20)
    private String priority;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "description_source", length = 30)
    private String descriptionSource;

    @Column(name = "is_template", nullable = false)
    private Boolean isTemplate = false;

    @Column(name = "template_name", length = 255)
    private String templateName;

    @Column(name = "ats_keywords", columnDefinition = "TEXT")
    private String atsKeywords;

    @Column(name = "openings_count", nullable = false)
    private Short openingsCount = 1;

    @Column(name = "filled_count", nullable = false)
    private Short filledCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Employee owner;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Submission> submissions = new HashSet<>();

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Interview> interviews = new HashSet<>();

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Offer> offers = new HashSet<>();

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Onboarding> onboardingRecords = new HashSet<>();

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<AiReview> aiReviews = new HashSet<>();

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
