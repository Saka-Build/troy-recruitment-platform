package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Sub-Status entity for Troy ATS
 */
@Entity
@Table(name = "sub_statuses", uniqueConstraints = {
    @UniqueConstraint(name = "uk_sub_statuses_status_name", columnNames = {"status_id", "name"})
}, indexes = {
    @Index(name = "idx_sub_statuses_status_id", columnList = "status_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "colour_hex", length = 7)
    private String colourHex;

    @Column(name = "sort_order", nullable = false)
    private Short sortOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "subStatus", fetch = FetchType.LAZY)
    private Set<Candidate> candidatesWithSubStatus = new HashSet<>();

    @OneToMany(mappedBy = "subStatus", fetch = FetchType.LAZY)
    private Set<Submission> submissionsWithSubStatus = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
