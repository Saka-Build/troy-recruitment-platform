package com.troy.ats.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CandidateCreateRequest {

    private String fullName;
    private String email;
    private String phone;
    private String whatsapp;
    private String location;
    private String nationality;
    private String cvId;

    private String currentDesignation;
    private String currentEmployer;

    private BigDecimal experienceYears;
    private Short noticePeriodDays;

    private BigDecimal currentSalary;
    private BigDecimal expectedSalary;
    private String salaryCurrency;

    private String[] skills;
    private String education;

    private String visaStatus;
    private String linkedinUrl;
    private String source;

    private UUID statusId;
    private UUID subStatusId;
    private UUID cvOwnerId;

    private String referredBy;
    private String referenceNote;

}
