package com.troy.ats.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
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
    private String pipelineStage;
    private String BDM;
    private UUID statusId;
    private String statusName;
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


}
