package com.troy.ats.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CandidateCreateRequest {

    private String cvId;
    private String fullName;
    private String currentDesignation;
    private UUID cvOwnerId;
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

}
