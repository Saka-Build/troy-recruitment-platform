package com.troy.ats.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SubmissionCreateRequest {

    private UUID candidateId;
    private UUID jobId;
    private String pipelineStage;
    private UUID statusId;
    private UUID subStatusId;
    private String notes;


}
