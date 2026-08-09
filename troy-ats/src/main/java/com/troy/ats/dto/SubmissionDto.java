package com.troy.ats.dto;

import com.troy.ats.enums.PipelineStage;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionDto {
    private UUID id;
    
    @NotNull(message = "Candidate is required")
    private UUID candidateId;
    
    @NotNull(message = "Job is required")
    private UUID jobId;
    
    private PipelineStage pipelineStage;
    private UUID statusId;
    private UUID subStatusId;
    private UUID submittedBy;
    private LocalDateTime submittedAt;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

