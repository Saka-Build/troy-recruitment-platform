package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "end_clients",
        uniqueConstraints = {@UniqueConstraint(name = "uk_end_clients_name", columnNames = "name")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndClient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}