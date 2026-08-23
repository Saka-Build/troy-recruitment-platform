package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "activity_log",
        indexes = {
                @Index(
                        name = "idx_activity_entity",
                        columnList = "entity_type, entity_id"
                ),
                @Index(
                        name = "idx_activity_performed_at",
                        columnList = "performed_at"
                ),
                @Index(
                        name = "idx_activity_performed_by",
                        columnList = "performed_by"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "old_value", columnDefinition = "jsonb")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "jsonb")
    private String newValue;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "performed_by",
            foreignKey = @ForeignKey(name = "fk_activity_log_performed_by")
    )
    private Employee performedBy;

    @Column(name = "performed_at", nullable = false)
    private Instant performedAt;

    @PrePersist
    protected void onCreate() {
        if (performedAt == null) {
            performedAt = Instant.now();
        }
    }
}