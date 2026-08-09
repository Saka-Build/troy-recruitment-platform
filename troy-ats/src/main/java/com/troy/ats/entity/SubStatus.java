package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "sub_statuses",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sub_status_status_name",
                        columnNames = {"status_id", "name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "status_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_sub_status_status")
    )
    private Status status;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "colour_hex", length = 7)
    private String colourHex;

    @Column(name = "sort_order", nullable = false)
    private Short sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();

        if (sortOrder == null) {
            sortOrder = 0;
        }

        if (active == null) {
            active = true;
        }
    }
}