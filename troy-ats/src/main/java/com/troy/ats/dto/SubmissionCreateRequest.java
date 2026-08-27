package com.troy.ats.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SubmissionCreateRequest {

    private UUID candidateId;
    private UUID jobId;
    private String pipelineStage;
    private UUID statusId;
    private UUID subStatusId;
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

    private List<ActivityLogRequest> activityLogs;


}
