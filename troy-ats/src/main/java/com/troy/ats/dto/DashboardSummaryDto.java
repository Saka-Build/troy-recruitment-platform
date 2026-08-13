package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {

    private long totalCandidates;
    private long openJobs;
    private long activeClients;
    private long totalPlacements;
    private long totalInterviewsToday;
    private long totalCvSubmissionPending;
    private long totalOffersPending;
    private long totalJoiningToday;
    private long totalUrgentRoles;
    private long totalClientFeedbackPending;
    private long totalOfferAwaitingCandidateResponse;
    private InterviewDataForDashboardDto earliestInterview;
    private List<InterviewDataForDashboardDto> todayInterviews;
    private List<String> candidatesNotConfirmed;

}
