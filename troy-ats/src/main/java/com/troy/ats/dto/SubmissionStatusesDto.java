package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionStatusesDto {

    List<SubmissionStatus> submissionStatusList;
    List<String> workflowStages;
}
