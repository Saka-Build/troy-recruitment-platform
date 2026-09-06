package com.troy.ats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class SubmissionDto {

    private UUID submissionId;
    private UUID candidateId;
    private String candidateName;
    private String candidateDesignation;
    private String candidateCVId;
    private String candidateOriginalCV;
    private String candidateEmail;
    private String candidatePhone;
    private UUID jobId;
    private String troyJobId;
    private String jobName;
    private String jobPriority;
    private String clientName;
    private String endClientName;
    @JsonProperty("BDM")
    private String bdm;
    private String pipelineStage;
    private UUID statusId;
    private String statusName;
    private String statusUpdatedAt;
    private String statusUpdatedBy;
    private UUID subStatusId;
    private String subStatusName;
    BigDecimal candidateExpectedAmount;
    String candidateExpectedCurrency;
    String candidateExpectedPeriod;
    BigDecimal submissionAmount;
    String submissionCurrency;
    String submissionPeriod;
    BigDecimal offerAmount;
    String offerCurrency;
    String offerPeriod;
    private String notes;
    private long historyCounts;
    private NoteDto latestCandidateNote;


}
