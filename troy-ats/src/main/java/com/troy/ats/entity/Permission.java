package com.troy.ats.entity;

import com.troy.ats.enums.PermissionAction;
import com.troy.ats.enums.PermissionModule;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "permissions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permission_module_action",
                        columnNames = {
                                "module",
                                "action"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_permission_module",
                        columnList = "module"
                ),
                @Index(
                        name = "idx_permission_action",
                        columnList = "action"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "module",
            nullable = false,
            length = 50
    )
    private PermissionModule module;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "action",
            nullable = false,
            length = 50
    )
    private PermissionAction action;

    @Column(
            name = "description",
            length = 255
    )
    private String description;
}