package com.troy.ats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "countries",
        indexes = {
                @Index(name = "idx_countries_code", columnList = "code"),
                @Index(name = "idx_countries_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_countries_code", columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 3)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
