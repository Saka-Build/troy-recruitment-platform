package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogRequest {

    private String entityType;
    private UUID entityId;
    private String field;
    private String oldValue;
    private String newValue;
    private String action;
    private String description;
}
