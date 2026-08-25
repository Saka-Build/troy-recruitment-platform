package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "role_permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_role_permissions",
                        columnNames = {
                                "role_id",
                                "permission_id"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_role_permissions_role",
                        columnList = "role_id"
                ),
                @Index(
                        name = "idx_role_permissions_permission",
                        columnList = "permission_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "role_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_role_permissions_role"
            )
    )
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "permission_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_role_permissions_permission"
            )
    )
    private Permission permission;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}