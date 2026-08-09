package com.troy.ats.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.troy.ats.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Employee entity for Troy ATS
 */
@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_employees_official_email", columnList = "official_email"),
    @Index(name = "idx_employees_role", columnList = "role"),
    @Index(name = "idx_employees_is_active", columnList = "is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "designation", nullable = false, length = 150)
    private String designation;

    @Column(name = "official_email", nullable = false, unique = true, length = 255)
    private String officialEmail;

    @Column(name = "personal_email", unique = true, length = 255)
    private String personalEmail;

    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Column(name = "whatsapp", nullable = false, length = 30)
    private String whatsapp;

    @Column(name = "photo_url")
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Set<Client> createdClients = new HashSet<>();

    @OneToMany(mappedBy = "cvOwner", fetch = FetchType.LAZY)
    private Set<Candidate> ownedCandidates = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Set<Candidate> createdCandidates = new HashSet<>();

    @OneToMany(mappedBy = "updatedBy", fetch = FetchType.LAZY)
    private Set<Candidate> updatedCandidates = new HashSet<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Job> ownedJobs = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Set<Job> createdJobs = new HashSet<>();

    @OneToMany(mappedBy = "updatedBy", fetch = FetchType.LAZY)
    private Set<Job> updatedJobs = new HashSet<>();

    @OneToMany(mappedBy = "submittedBy", fetch = FetchType.LAZY)
    private Set<Submission> submittedSubmissions = new HashSet<>();

    @OneToMany(mappedBy = "scheduledBy", fetch = FetchType.LAZY)
    private Set<Interview> scheduledInterviews = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Set<Offer> createdOffers = new HashSet<>();

    @OneToMany(mappedBy = "managedBy", fetch = FetchType.LAZY)
    private Set<Onboarding> managedOnboarding = new HashSet<>();

    @OneToMany(mappedBy = "performedBy", fetch = FetchType.LAZY)
    private Set<ActivityLog> performedLogs = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Set<Note> createdNotes = new HashSet<>();

    @OneToMany(mappedBy = "sentBy", fetch = FetchType.LAZY)
    private Set<Communication> sentCommunications = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private Set<MessageTemplate> createdTemplates = new HashSet<>();

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
