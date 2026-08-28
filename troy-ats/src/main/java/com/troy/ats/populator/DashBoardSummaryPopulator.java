package com.troy.ats.populator;

import com.troy.ats.constants.CommonConstants;
import com.troy.ats.dto.DashboardSummaryDto;
import com.troy.ats.dto.InterviewDataForDashboardDto;
import com.troy.ats.entity.Interview;
import com.troy.ats.entity.Submission;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.service.*;
import com.troy.ats.util.CommonUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.troy.ats.constants.CommonConstants.*;
import static com.troy.ats.util.CommonUtil.enumToStringFormat;

@Component
public class DashBoardSummaryPopulator {

    @Resource(name="candidateService")
    private CandidateService candidateService;

    @Resource(name="jobService")
    private JobService jobService;

    @Resource(name="clientService")
    private ClientService clientService;

    @Resource(name="sessionService")
    private SessionService sessionService;

    @Resource(name="interviewService")
    private InterviewService interviewService;

    @Resource(name="submissionService")
    private SubmissionService submissionService;

    @Resource(name="offerService")
    private OfferService offerService;



    public void populate(DashboardSummaryDto dashboardSummaryDto) {

        dashboardSummaryDto.setTotalCandidates(candidateService.getTotalCandidatesByStatusName(CANDIDATE_STATUS_ACTIVE));
        dashboardSummaryDto.setOpenJobs(jobService.getTotalJobsByStatus(JobStatus.OPEN));
        dashboardSummaryDto.setActiveClients(clientService.getTotalClientsByActive(Boolean.TRUE));
        dashboardSummaryDto.setTotalPlacements(submissionService.getTotalCVSubmissionsByPipelineStage(PipelineStage.ONBOARDED));
        dashboardSummaryDto.setTotalCvSubmissionPending(submissionService.getTotalCVSubmissionsByPipelineStage(PipelineStage.READY_TO_SUBMIT));
       // dashboardSummaryDto.setTotalJoiningToday(offerService.getTotalJoiningTodayForZoneId(CommonUtil.getZoneIdForCurrentUser(sessionService)));
        dashboardSummaryDto.setTotalUrgentRoles(jobService.getTotalJobsByPriority(CommonConstants.JOB_PRIORITY_URGENT));
        //dashboardSummaryDto.setTotalClientFeedbackPending(interviewService.getTotalClientFeedBackPending());
        long L1FeedBackPending = submissionService.countSubmissionsByStatusAndSubStatus(SUBMISSION_STATUS_INTERVIEW,SUBMISSION_SUBSTATUS_L1FEEDBACK);
        long L2FeedBackPending = submissionService.countSubmissionsByStatusAndSubStatus(SUBMISSION_STATUS_INTERVIEW,SUBMISSION_SUBSTATUS_L2FEEDBACK);
        long totalCountsPending = L1FeedBackPending + L2FeedBackPending;
        dashboardSummaryDto.setTotalClientFeedbackPending(totalCountsPending);
        //dashboardSummaryDto.setTotalOffersPending(offerService.getTotalOffersByStaus(OfferStatus.PENDING));
        dashboardSummaryDto.setTotalOffersPending(submissionService.countSubmissionsByStatus(SUBMISSION_STATUS_SELECTED));
       // dashboardSummaryDto.setTotalOfferAwaitingCandidateResponse(offerService.getTotalOffersByStaus(OfferStatus.RELEASED));
        dashboardSummaryDto.setTotalOfferAwaitingCandidateResponse(submissionService.countSubmissionsByStatusAndSubStatus(SUBMISSION_STATUS_OFFER_RELEASED,SUBMISSION_SUBSTATUS_CANDIDATE_CONSENT_AWAITING));

        List<String> candidatesNotConfirmed = new ArrayList<>();
        //List<Candidate> candidates = candidateService.getCandidatesByStatusNameANDSubStatusName(CommonConstants.CANDIDATE_STATUS_READY_TO_SUBMIT, CommonConstants.CANDIDATE_SUBSTATUS_CANDIDATE_CONFIRMATION_AWAITED);
        //candidatesNotConfirmed = candidates.stream().map(Candidate::getFullName).toList();

        List<Submission> submissionList = submissionService.findByStatus_NameIgnoreCaseAndSubStatus_NameIgnoreCase(SUBMISSION_STATUS_READY_TO_SUBMIT, SUBMISSION_SUBSTATUS_CANDIDATE_CONF_AWAITING);
        candidatesNotConfirmed = submissionList.stream().map(submission -> {
                                                            return submission.getCandidate().getFullName();
                                                        }).toList();
        dashboardSummaryDto.setCandidatesNotConfirmed(candidatesNotConfirmed);

        populateTodayInterviews(dashboardSummaryDto);



    }

    private void populateTodayInterviews(DashboardSummaryDto dashboardSummaryDto){

        ZoneId zoneId = CommonUtil.getZoneIdForCurrentUser(sessionService);
        List<Interview> todayInterviews = interviewService.getTodayInterviewsForZoneIdWithDescOrder(zoneId);

        todayInterviews.sort(Comparator.comparing(
                        Interview::getInterviewDateTimeWithZone,
                        Comparator.reverseOrder()
                )
        );
        List<InterviewDataForDashboardDto> todayInterviewList = new ArrayList<InterviewDataForDashboardDto>();
        for(Interview interview : CollectionUtils.emptyIfNull(todayInterviews)){
            InterviewDataForDashboardDto interviewDataForDashboardDto = new InterviewDataForDashboardDto();
            populateInterview(interview,interviewDataForDashboardDto);
            todayInterviewList.add(interviewDataForDashboardDto);

        }
        dashboardSummaryDto.setTotalInterviewsToday(todayInterviewList.size());
        dashboardSummaryDto.setTodayInterviews(todayInterviewList);
        if(todayInterviewList.size() > 0){
            dashboardSummaryDto.setEarliestInterview(todayInterviewList.get(0));
        }
    }

    private void populateInterview(Interview source, InterviewDataForDashboardDto target){

        Instant instant = source.getInterviewDateTimeWithZone();
        ZoneId zoneId = CommonUtil.getZoneIdForCurrentUser(sessionService);
        ZonedDateTime DateTime = instant.atZone(zoneId);
        LocalDate date = DateTime.toLocalDate();
        LocalTime time = DateTime.toLocalTime();

        target.setInterviewDate(date);
        target.setInterviewTime(time);
        target.setInterviewerName(source.getInterviewerName());
        target.setCandidateName(source.getCandidate().getFullName());
        target.setJobName(source.getJob().getClient().getName());
        target.setInterviewType(enumToStringFormat(source.getInterviewType().name()));
        target.setInterviewStatus(enumToStringFormat(source.getOutcome().name()));
        target.setInterviewLink(source.getMeetingLink());
    }
}
