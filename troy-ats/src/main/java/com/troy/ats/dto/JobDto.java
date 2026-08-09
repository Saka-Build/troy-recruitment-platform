package com.troy.ats.dto;

import com.troy.ats.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private UUID id;
    
    @NotBlank(message = "Job title is required")
    private String title;
    
    @NotNull(message = "Client is required")
    private UUID clientId;
    
    private String location;
    private String country;
    private JobWorkMode workMode;
    private JobType jobType;
    private String industry;
    
    @PositiveOrZero(message = "Minimum experience must be >= 0")
    private Double experienceMin;
    
    @PositiveOrZero(message = "Maximum experience must be >= 0")
    private Double experienceMax;
    
    @PositiveOrZero(message = "Minimum salary must be >= 0")
    private Double salaryMin;
    
    @PositiveOrZero(message = "Maximum salary must be >= 0")
    private Double salaryMax;
    
    private String salaryCurrency;
    
    private Set<String> skillsRequired;
    private JobStatus status;
    private String priority;
    
    private String description;
    private String descriptionSource;
    private Boolean isTemplate;
    private String templateName;
    private String atsKeywords;
    
    private Short openingsCount;
    private Short filledCount;
    
    private UUID ownerId;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private String createdByName;
}

