package com.troy.ats.populator;

import com.troy.ats.dto.DashboardSummaryDto;
import com.troy.ats.dto.InterviewDataForDashboardDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Interview;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.OfferStatus;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.repository.SubmissionRepository;
import com.troy.ats.service.*;
import com.troy.ats.util.CommonUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import com.troy.ats.constants.CommonConstants;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

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

        dashboardSummaryDto.setTotalCandidates(candidateService.getTotalCandidatesByActive(Boolean.TRUE));
        dashboardSummaryDto.setOpenJobs(jobService.getTotalJobsByStatus(JobStatus.OPEN));
        dashboardSummaryDto.setActiveClients(clientService.getTotalClientsByActive(Boolean.TRUE));
        dashboardSummaryDto.setTotalPlacements(candidateService.getTotalCandidatesByStatusName(CommonConstants.CANDIDATE_STATUS_ONBOARDED));
       // dashboardSummaryDto.setTotalCvSubmissionPending(submissionService.getTotalCVSubmissionsByPipelineStage(PipelineStage.READY_TO_SUBMIT));
       // dashboardSummaryDto.setTotalJoiningToday(offerService.getTotalJoiningTodayForZoneId(CommonUtil.getZoneIdForCurrentUser(sessionService)));
        dashboardSummaryDto.setTotalUrgentRoles(jobService.getTotalJobsByPriority(CommonConstants.JOB_PRIORITY_URGENT));
       // dashboardSummaryDto.setTotalClientFeedbackPending(interviewService.getTotalClientFeedBackPending());
        //dashboardSummaryDto.setTotalOffersPending(offerService.getTotalOffersByStaus(OfferStatus.PENDING));
       // dashboardSummaryDto.setTotalOfferAwaitingCandidateResponse(offerService.getTotalOffersByStaus(OfferStatus.RELEASED));

        List<String> candidatesNotConfirmed = new ArrayList<>();
        List<Candidate> candidates = candidateService.getCandidatesByStatusNameANDSubStatusName(CommonConstants.CANDIDATE_STATUS_READY_TO_SUBMIT, CommonConstants.CANDIDATE_SUBSTATUS_CANDIDATE_CONFIRMATION_AWAITED);
        candidatesNotConfirmed = candidates.stream().map(Candidate::getFullName).toList();
        dashboardSummaryDto.setCandidatesNotConfirmed(candidatesNotConfirmed);

        populateTodayInterviews(dashboardSummaryDto);



    }

    private void populateTodayInterviews(DashboardSummaryDto dashboardSummaryDto){

        ZoneId zoneId = CommonUtil.getZoneIdForCurrentUser(sessionService);
        List<Interview> todayInterviews = interviewService.getTodayInterviewsForZoneIdWithDescOrder(zoneId);

        /*todayInterviews.sort(Comparator.comparing(
                        Interview::getInterviewDateTimeWithZone,
                        Comparator.reverseOrder()
                )
        );*/
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
        target.setInterviewLink(source.getMeetingLink());
    }
}
