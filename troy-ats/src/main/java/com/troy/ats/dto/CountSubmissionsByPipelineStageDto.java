package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountSubmissionsByPipelineStageDto {

    long totalApplied;
    long totalScreening;
    long totalReadyToSubmit;
    long totalSubmitted;
    long totalInterview;
    long totalSelected;
    long totalRejected;
    long totalOnBoarding;
    long totalOnBoarded;

}
