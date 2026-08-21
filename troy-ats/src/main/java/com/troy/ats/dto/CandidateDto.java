package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDto {

    UUID id;
    private String cvId;
    private String fullName;
    private String currentDesignation;
    private UUID cvOwnerId;
    private String cvOwnerName;
    private String referredBy;
    private String referenceNote;
    private String email;
    private String phone;
    private String whatsapp;
    private String location;
    private String nationality;
    private String currentEmployer;
    private BigDecimal experienceYears;
    private String[] skills;
    private Short noticePeriodDays;
    private String visaStatus;
    private String source;
    private String linkedinUrl;
    private String status;
    private String education;
    BigDecimal currentSalaryAmount;
    String currentSalaryCurrency;
    String currentSalaryPeriod;
    BigDecimal expectedSalaryAmount;
    String expectedSalaryCurrency;
    String expectedSalaryPeriod;
    OffsetDateTime createdAt;
}
