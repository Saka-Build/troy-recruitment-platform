package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "statuses",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_status_name", columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "colour_hex", nullable = false, length = 7)
    private String colourHex;

    @Column(name = "sort_order", nullable = false)
    private Short sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "show_in_pipeline", nullable = false)
    private Boolean showInPipeline = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(
            mappedBy = "status",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<SubStatus> subStatuses = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();

        if (colourHex == null) {
            colourHex = "#6B7280";
        }

        if (sortOrder == null) {
            sortOrder = 0;
        }

        if (active == null) {
            active = true;
        }
    }

    public void addSubStatus(SubStatus subStatus) {
        subStatuses.add(subStatus);
        // subStatus.setStatus(this);
    }

    public void removeSubStatus(SubStatus subStatus) {
        subStatuses.remove(subStatus);
        //subStatus.setStatus(null);
    }
}