package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Status entity for Troy ATS
 */
@Entity
@Table(name = "statuses", uniqueConstraints = {
    @UniqueConstraint(name = "uk_statuses_name_active", columnNames = {"name"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "colour_hex", nullable = false, length = 7)
    private String colourHex;

    @Column(name = "sort_order", nullable = false)
    private Short sortOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private Set<SubStatus> subStatuses = new HashSet<>();

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private Set<Candidate> candidatesWithStatus = new HashSet<>();

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private Set<Submission> submissionsWithStatus = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
