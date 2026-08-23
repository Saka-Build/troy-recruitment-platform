package com.troy.ats.dto;

import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.JobType;
import com.troy.ats.enums.JobWorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

    UUID id;
    String jobId;
    String title;
    UUID clientId;
    String clientName;
    UUID endClientId;
    String endClientName;
    UUID countryId;
    String countryCode;
    String countryName;
    String location;
    String workMode;
    String jobType;
    BigDecimal clientRateAmount;
    String clientRateCurrency;
    String clientRatePeriod;
    BigDecimal candidateRateAmount;
    String candidateRateCurrency;
    String candidateRatePeriod;
    String[] skillsRequired;
    String status;
    String priority;
    String leadNote;
    String description;
    String descriptionSource;
    String industry;
    Boolean isTemplate;
    String templateName;
    BigDecimal experienceMin;
    BigDecimal experienceMax;
    UUID ownerId;
    String ownerName;
    List<EmployeeDto> assignedRecruiters;

    /*BigDecimal salaryMin;
    BigDecimal salaryMax;
    String salaryCurrency;
    String[] atsKeywords;
    Short openingsCount;
    Short filledCount;*/

}
