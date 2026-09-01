package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionFiltersDto {

    long totalSubmittedApplications;
    long totalInterviewApplications;
    long totalOnboardedApplications;

    List<JobsForSubmissionFiltersDto> jobs;
    List<ClientsForSubmissionFiltersDto> clients;
    List<SubmissionStatus> applicationStatusList;

}
