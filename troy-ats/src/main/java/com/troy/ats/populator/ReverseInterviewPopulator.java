package com.troy.ats.populator;

import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.dto.InterviewScheduleRequest;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Interview;
import com.troy.ats.entity.Job;
import com.troy.ats.entity.Submission;
import com.troy.ats.enums.InterviewOutcome;
import com.troy.ats.enums.InterviewRound;
import com.troy.ats.enums.InterviewType;
import com.troy.ats.service.CandidateService;
import com.troy.ats.service.SessionService;
import com.troy.ats.service.SubmissionService;
import com.troy.ats.service.impl.JobServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.troy.ats.util.CommonUtil.*;


@Component
@RequiredArgsConstructor
public class ReverseInterviewPopulator {


    private final CandidateService candidateService;
    private final JobServiceImpl jobService;
    private final SubmissionService submissionService;


    public void populate(InterviewScheduleRequest source, Interview target, SessionService sessionService) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
        List<ActivityLogRequest> activityLogs = new ArrayList<>();
        String entityType = Submission.class.getSimpleName().toLowerCase(Locale.ROOT);
        UUID entityId = source.getSubmissionId();

        if(Objects.nonNull(source.getSubmissionId())){
            Submission submission = submissionService.getSubmissionById(source.getSubmissionId());
            target.setSubmission(submission);

        }

        if(Objects.nonNull(source.getCandidateId())){
            Candidate candidate = candidateService.getCandidateById(source.getCandidateId());
            target.setCandidate(candidate);
        }

        if(Objects.nonNull(source.getJobId())) {
            Job job = jobService.getJobById(source.getJobId());
            target.setJob(job);
        }


        if (source.getInterviewDate() != null) {
            target.setInterviewDate(source.getInterviewDate());
        }

        if (source.getInterviewTime() != null) {
            target.setInterviewTime(LocalTime.parse(source.getInterviewTime().trim(), formatter));
        }

        if (source.getInterviewType() != null) {
            String oldValue = Objects.nonNull(target.getInterviewType()) ? target.getInterviewType().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Interview.class, "setInterviewType", InterviewType.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getInterviewType(),activityLogs);
            target.setInterviewType(InterviewType.fromValue(source.getInterviewType()));
        }

        if (source.getRound() != null) {
            String oldValue = Objects.nonNull(target.getRound()) ? target.getRound().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Interview.class, "setRound", InterviewRound.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getRound(),activityLogs);
            target.setRound(InterviewRound.fromValue(source.getRound()));
        }

        if (source.getInterviewerName() != null) {
            String oldValue = Objects.nonNull(target.getInterviewerName()) ? target.getInterviewerName() : null;
            String field = getFieldName(Interview.class, "setInterviewerName", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getInterviewerName(),activityLogs);
            target.setInterviewerName(source.getInterviewerName());
        }

        if (source.getStatus() != null) {
            String oldValue = Objects.nonNull(target.getOutcome()) ? target.getOutcome().name() : null;
            String field = getFieldName(Interview.class, "setOutcome", InterviewOutcome.class);
            populateActivityLog(entityType, entityId, field, oldValue, source.getStatus().toLowerCase(Locale.ROOT),activityLogs);
            target.setOutcome(InterviewOutcome.fromValue(source.getStatus()));
        }

        ZoneId zone = getZoneIdForCurrentUser(sessionService);
        String interViewField = "Interview Date Time";


        if(Objects.nonNull(source.getInterviewDate()) && Objects.nonNull(source.getInterviewTime())){
            Instant interviewInstant = LocalDateTime
                    .of(
                            source.getInterviewDate(),
                            LocalTime.parse(source.getInterviewTime().trim(), formatter)
                    )
                    .atZone(zone)
                    .toInstant();
            String oldValue = Objects.nonNull(target.getInterviewDateTimeWithZone()) ? target.getInterviewDateTimeWithZone().toString() : null;
            populateActivityLog(entityType, entityId, interViewField, oldValue,interviewInstant.toString(),activityLogs);
            target.setInterviewDateTimeWithZone(interviewInstant);

        } else if(Objects.nonNull(source.getInterviewDate())){
            Instant interviewInstant = LocalDateTime
                    .of(
                            source.getInterviewDate(),
                            target.getInterviewTime()
                    )
                    .atZone(zone)
                    .toInstant();
            String oldValue = Objects.nonNull(target.getInterviewDateTimeWithZone()) ? target.getInterviewDateTimeWithZone().toString() : null;
            populateActivityLog(entityType, entityId, interViewField, oldValue,interviewInstant.toString(),activityLogs);
            target.setInterviewDateTimeWithZone(interviewInstant);

        } else if(Objects.nonNull(source.getInterviewTime())){
            Instant interviewInstant = LocalDateTime
                    .of(
                            target.getInterviewDate(),
                            LocalTime.parse(source.getInterviewTime().trim(), formatter)
                    )
                    .atZone(zone)
                    .toInstant();
            String oldValue = Objects.nonNull(target.getInterviewDateTimeWithZone()) ? target.getInterviewDateTimeWithZone().toString() : null;
            populateActivityLog(entityType, entityId, interViewField, oldValue,interviewInstant.toString(),activityLogs);
            target.setInterviewDateTimeWithZone(interviewInstant);
        }


        source.setActivityLogs(activityLogs);
    }

}