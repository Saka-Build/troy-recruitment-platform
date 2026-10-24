package com.troy.ats.populator;

import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.dto.InterviewDto;
import com.troy.ats.dto.InterviewScheduleRequest;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Interview;
import com.troy.ats.entity.Job;
import com.troy.ats.entity.Submission;
import com.troy.ats.enums.InterviewRound;
import com.troy.ats.enums.InterviewType;
import com.troy.ats.service.CandidateService;
import com.troy.ats.service.SubmissionService;
import com.troy.ats.service.impl.JobServiceImpl;
import com.troy.ats.service.impl.SessionServiceImpl;
import com.troy.ats.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.troy.ats.util.CommonUtil.*;
import static com.troy.ats.util.CommonUtil.enumToStringFormat;


@Component
@RequiredArgsConstructor
public class InterviewPopulator {

    private final SessionServiceImpl sessionService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);


    public void populate(Interview source, InterviewDto target) {

        target.setId(source.getId());

        Instant instant = source.getInterviewDateTimeWithZone();
        ZoneId zoneId = CommonUtil.getZoneIdForCurrentUser(sessionService);
        ZonedDateTime DateTime = instant.atZone(zoneId);
        LocalDate date = DateTime.toLocalDate();
        String time = DateTime.toLocalTime().format(formatter);

        target.setInterviewDate(date);
        target.setInterviewTime(time);
        target.setInterviewerName(source.getInterviewerName());

        target.setInterviewType(enumToStringFormat(source.getInterviewType().name()));
        target.setRound(enumToStringFormat(source.getRound().name()));
        target.setStatus(enumToStringFormat(source.getOutcome().name()));

        //target.setSubmissionId(source.getSubmission().getId());
        //target.setCandidateId(source.getCandidate().getId());
        //target.setJobId(source.getJob().getId());
    }

}