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
    String countryCode;
    String location;
    JobWorkMode workMode;
    JobType jobType;
    String industry;
    BigDecimal experienceMin;
    BigDecimal experienceMax;
    BigDecimal salaryMin;
    BigDecimal salaryMax;
    String salaryCurrency;
    String[] skillsRequired;
    JobStatus status;
    String priority;
    String description;
    String descriptionSource;
    Boolean isTemplate;
    String templateName;
    String[] atsKeywords;
    Short openingsCount;
    Short filledCount;
    UUID ownerId;

}
