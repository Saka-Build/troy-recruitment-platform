package com.troy.ats.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.troy.ats.enums.CvFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Candidate entity for Troy ATS
 */
@Entity
@Table(name = "candidates", indexes = {
    @Index(name = "idx_candidates_skills_gin", columnList = "skills", columnDefinition = "text[]"),
    @Index(name = "idx_candidates_full_name", columnList = "full_name"),
    @Index(name = "idx_candidates_status_id", columnList = "status_id"),
    @Index(name = "idx_candidates_cv_owner_id", columnList = "cv_owner_id"),
    @Index(name = "idx_candidates_location", columnList = "location"),
    @Index(name = "idx_candidates_created_at", columnList = "created_at DESC"),
    @Index(name = "idx_candidates_is_active", columnList = "is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "cv_id", nullable = false, unique = true, length = 50)
    private String cvId;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "whatsapp", length = 30)
    private String whatsapp;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "current_designation", length = 150)
    private String currentDesignation;

    @Column(name = "current_employer", length = 255)
    private String currentEmployer;

    @Column(name = "experience_years", precision = 4)
    private Double experienceYears;

    @Column(name = "notice_period_days")
    private Integer noticePeriodDays;

    @Column(name = "current_salary", precision = 14, scale = 2)
    private Double currentSalary;

    @Column(name = "expected_salary", precision = 14, scale = 2)
    private Double expectedSalary;

    @Column(name = "salary_currency", length = 3)
    private String salaryCurrency;

    @ElementCollection
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "skill", length = 100)
    private Set<String> skills = new HashSet<>();

    @Column(name = "education", columnDefinition = "TEXT")
    private String education;

    @Column(name = "visa_status", length = 100)
    private String visaStatus;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "source", length = 100)
    private String source;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_status_id")
    private SubStatus subStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cv_owner_id", nullable = false)
    private Employee cvOwner;

    @Column(name = "referred_by", length = 255)
    private String referredBy;

    @Column(name = "reference_note", columnDefinition = "TEXT")
    private String referenceNote;

    @Column(name = "original_cv_url")
    private String originalCvUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "original_cv_format")
    private CvFormat originalCvFormat;

    @Column(name = "troy_cv_url")
    private String troyCvUrl;

    @Column(name = "troy_cv_pdf_url")
    private String troyCvPdfUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Employee updatedBy;

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private Set<Submission> submissions = new HashSet<>();

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private Set<Interview> interviews = new HashSet<>();

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private Set<Offer> offers = new HashSet<>();

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
    private Set<Onboarding> onboardingRecords = new HashSet<>();

    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY)
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
