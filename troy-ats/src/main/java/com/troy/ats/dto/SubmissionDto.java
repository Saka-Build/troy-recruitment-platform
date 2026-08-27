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
    private UUID jobId;
    private String jobName;
    private String clientName;
    private String pipelineStage;
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
