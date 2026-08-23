package com.troy.ats.populator;

import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.dto.SubmissionCreateRequest;
import com.troy.ats.entity.*;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.repository.StatusRepository;
import com.troy.ats.service.CandidateService;
import com.troy.ats.service.impl.JobServiceImpl;
import com.troy.ats.service.impl.SubmissionStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

import static com.troy.ats.constants.CommonConstants.*;
import static com.troy.ats.util.CommonUtil.getFieldName;
import static com.troy.ats.util.CommonUtil.populateActivityLog;


@Component
@RequiredArgsConstructor
public class ReverseSubmissionPopulator {


    private final CandidateService candidateService;
    private final JobServiceImpl jobService;
    private final StatusRepository statusRepository;
    private final SubmissionStatusServiceImpl submissionStatusService;

    public void populate(SubmissionCreateRequest source, Submission target) {

        List<ActivityLogRequest> activityLogs = new ArrayList<>();
        String entityType = target.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        UUID entityId = target.getId();

        if(Objects.nonNull(source.getCandidateId())){
            Candidate candidate = candidateService.getCandidateById(source.getCandidateId());
            target.setCandidate(candidate);
        }

        if(Objects.nonNull(source.getJobId())) {
            Job job = jobService.getJobById(source.getJobId());
            target.setJob(job);
        }
        if(Objects.nonNull(source.getPipelineStage())) {
            String oldValue = Objects.nonNull(target.getPipelineStage()) ? target.getPipelineStage().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Submission.class, "setPipelineStage", PipelineStage.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getPipelineStage(),activityLogs);
            target.setPipelineStage(PipelineStage.fromValue(source.getPipelineStage()));
        }
        if(Objects.nonNull(source.getStatusId())) {
            Status status = submissionStatusService.getStatusById(source.getStatusId());
            String field = getFieldName(Submission.class, "setStatus", Status.class);
            String oldValue = Objects.nonNull(target.getStatus()) ? target.getStatus().getName() : null;
            populateActivityLog(entityType, entityId, field, oldValue, status.getName(),activityLogs);
            target.setStatus(status);
        }
        if(Objects.nonNull(source.getSubStatusId())) {
            SubStatus subStatus = submissionStatusService.getSubStatusById(source.getSubStatusId());
            String field = getFieldName(Submission.class, "setSubStatus", SubStatus.class);
            String oldValue = Objects.nonNull(target.getSubStatus()) ? target.getSubStatus().getName() : null;
            populateActivityLog(entityType, entityId, field, oldValue, subStatus.getName(),activityLogs);
            target.setSubStatus(subStatus);
        }

        if(Objects.nonNull(source.getNotes())) {
            target.setNotes(source.getNotes());
        }

        source.setActivityLogs(activityLogs);
    }

}