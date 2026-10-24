package com.troy.ats.dto;

import com.troy.ats.entity.Employee;
import com.troy.ats.enums.CvFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogDto {

    private UUID id;
    private String entityType;
    private UUID entityId;
    private String action;
    private String oldValue;
    private String newValue;
    private String description;
    private String performedBy;
    private LocalDateTime performedAt;
}
