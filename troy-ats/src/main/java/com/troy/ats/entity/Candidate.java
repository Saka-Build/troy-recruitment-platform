package com.troy.ats.entity;

import com.troy.ats.enums.CvFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "candidates",
        indexes = {
                @Index(name = "idx_candidates_full_name", columnList = "full_name"),
                @Index(name = "idx_candidates_phone", columnList = "phone"),
                @Index(name = "idx_candidates_skills", columnList = "skills"),
                @Index(name = "idx_candidates_email", columnList = "email"),
                @Index(name = "idx_candidates_cv_owner", columnList = "cv_owner_id"),
                @Index(name = "idx_candidates_status", columnList = "status_id"),
                @Index(name = "idx_candidates_sub_status", columnList = "sub_status_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
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

    @Column(name = "experience_years", precision = 4, scale = 1)
    private BigDecimal experienceYears;

    @Column(name = "notice_period_days")
    private Short noticePeriodDays;

    @Column(name = "current_salary", precision = 14, scale = 2)
    private BigDecimal currentSalary;

    @Column(name = "expected_salary", precision = 14, scale = 2)
    private BigDecimal expectedSalary;

    @Column(name = "salary_currency", length = 3)
    private String salaryCurrency;

    @Column(name = "skills", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private String[] skills;

    @Column(name = "education", columnDefinition = "text")
    private String education;

    @Column(name = "visa_status", length = 100)
    private String visaStatus;

    @Column(name = "linkedin_url", columnDefinition = "text")
    private String linkedinUrl;

    @Column(name = "source", length = 100)
    private String source;

    // Status
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_status_id")
    private SubStatus subStatus;

    // Ownership
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cv_owner_id", nullable = false)
    private Employee cvOwner;

    @Column(name = "referred_by", length = 255)
    private String referredBy;

    @Column(name = "reference_note", columnDefinition = "text")
    private String referenceNote;

    // CV files
    @Column(name = "original_cv_url", columnDefinition = "text")
    private String originalCvUrl;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "original_cv_format")
    private CvFormat originalCvFormat;

    @Column(name = "troy_cv_url", columnDefinition = "text")
    private String troyCvUrl;

    @Column(name = "troy_cv_pdf_url", columnDefinition = "text")
    private String troyCvPdfUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Employee updatedBy;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (active == null) {
            active = true;
        }

        if (salaryCurrency == null) {
            salaryCurrency = "USD";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}