package com.troy.ats.dto;

import com.troy.ats.enums.CvFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDto {
    private UUID id;
    
    @NotBlank(message = "CV ID is required")
    private String cvId;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String phone;
    private String whatsapp;
    private String location;
    private String nationality;
    private String currentDesignation;
    private String currentEmployer;
    private Double experienceYears;
    private Integer noticePeriodDays;
    private Double currentSalary;
    private Double expectedSalary;
    private String salaryCurrency;
    
    @Size(max = 20, message = "Maximum 20 skills allowed")
    private Set<String> skills;
    
    private String education;
    private String visaStatus;
    private String linkedinUrl;
    private String source;
    
    @NotNull(message = "Status is required")
    private UUID statusId;
    
    private UUID subStatusId;
    
    @NotNull(message = "CV Owner is required")
    private UUID cvOwnerId;
    
    private String referredBy;
    private String referenceNote;
    private String originalCvUrl;
    private CvFormat originalCvFormat;
    private String troyCvUrl;
    private String troyCvPdfUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private String createdByName;
    private UUID updatedBy;
    private String updatedByName;
}

