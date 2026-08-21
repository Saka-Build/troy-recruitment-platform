package com.troy.ats.dto;

import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.JobType;
import com.troy.ats.enums.JobWorkMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class JobCreateRequest {

    String title;
    UUID clientId;
    UUID endClientId;
    String countryCode;
    String location;
    String jobType;
    String workMode;
    BigDecimal clientRateAmount;
    String clientRateCurrency;
    String clientRatePeriod;
    BigDecimal candidateRateAmount;
    String candidateRateCurrency;
    String candidateRatePeriod;
    String[] skillsRequired;
    String priority;
    String status;
    UUID ownerId;
    UUID[] assignedRecruiters;
    String description;
    String descriptionSource;
    String industry;
    BigDecimal experienceMin;
    BigDecimal experienceMax;
    BigDecimal salaryMin;
    BigDecimal salaryMax;
    String salaryCurrency;
    Boolean isTemplate;
    String templateName;
    String[] atsKeywords;
    Short openingsCount;
    Short filledCount;

}
