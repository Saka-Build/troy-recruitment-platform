package com.troy.ats.dto;

import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.JobType;
import com.troy.ats.enums.JobWorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

    UUID id;
    String title;
    UUID clientId;
    String clientName;
    UUID countryId;
    String countryCode;
    String countryName;
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
    String ownerName;

}
