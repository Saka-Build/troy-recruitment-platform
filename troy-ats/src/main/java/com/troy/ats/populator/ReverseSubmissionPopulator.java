package com.troy.ats.populator;

import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.dto.SubmissionCreateRequest;
import com.troy.ats.entity.*;
import com.troy.ats.enums.Currency;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.enums.RatePeriod;
import com.troy.ats.repository.StatusRepository;
import com.troy.ats.service.CandidateService;
import com.troy.ats.service.impl.JobServiceImpl;
import com.troy.ats.service.impl.SubmissionStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
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
        if(Objects.nonNull(source.getPipelineStage()) || Objects.nonNull(source.getStatusId())){
            Status status = null;
            if(Objects.nonNull(source.getStatusId())){
                target.setPipelineStage(PipelineStage.fromValue(status.getName()));
            } else {
                status = submissionStatusService.getStatusByName(source.getPipelineStage());
            }
            target.setStatus(status);
            target.setPipelineStage(PipelineStage.fromValue(status.getName()));

            String field = getFieldName(Submission.class, "setStatus", Status.class);
            String oldValue = Objects.nonNull(target.getStatus()) ? target.getStatus().getName() : null;
            populateActivityLog(entityType, entityId, field, oldValue, status.getName(),activityLogs);
        }

        if(Objects.nonNull(source.getSubStatusId())) {
            SubStatus subStatus = submissionStatusService.getSubStatusById(source.getSubStatusId());
            String field = getFieldName(Submission.class, "setSubStatus", SubStatus.class);
            String oldValue = Objects.nonNull(target.getSubStatus()) ? target.getSubStatus().getName() : null;
            populateActivityLog(entityType, entityId, field, oldValue, subStatus.getName(),activityLogs);
            target.setSubStatus(subStatus);
        }

        if (source.getCandidateExpectedAmount() != null) {
            String oldValue = Objects.nonNull(target.getCandidateExpectedAmount()) ? target.getCandidateExpectedAmount().toString() : null;
            String field = getFieldName(Submission.class, "setCandidateExpectedAmount", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCandidateExpectedAmount().toString(),activityLogs);
            target.setCandidateExpectedAmount(source.getCandidateExpectedAmount());
        }

        if (source.getCandidateExpectedCurrency() != null) {
            String oldValue = Objects.nonNull(target.getCandidateExpectedCurrency()) ? target.getCandidateExpectedCurrency().name() : null;
            String field = getFieldName(Submission.class, "setCandidateExpectedCurrency", com.troy.ats.enums.Currency.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCandidateExpectedCurrency(),activityLogs);
            target.setCandidateExpectedCurrency(Currency.fromValue(source.getCandidateExpectedCurrency()));
        }

        if (source.getCandidateExpectedPeriod() != null) {
            String oldValue = Objects.nonNull(target.getCandidateExpectedPeriod()) ? target.getCandidateExpectedPeriod().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Submission.class, "setCandidateExpectedPeriod", RatePeriod.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCandidateExpectedPeriod(),activityLogs);
            target.setCandidateExpectedPeriod(RatePeriod.fromValue(source.getCandidateExpectedPeriod()));
        }

        if (source.getSubmissionAmount() != null) {
            String oldValue = Objects.nonNull(target.getSubmissionAmount()) ? target.getSubmissionAmount().toString() : null;
            String field = getFieldName(Submission.class, "setSubmissionAmount", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getSubmissionAmount().toString(),activityLogs);
            target.setSubmissionAmount(source.getSubmissionAmount());
        }

        if (source.getSubmissionCurrency() != null) {
            String oldValue = Objects.nonNull(target.getSubmissionCurrency()) ? target.getSubmissionCurrency().name() : null;
            String field = getFieldName(Submission.class, "setSubmissionCurrency", com.troy.ats.enums.Currency.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getSubmissionCurrency(),activityLogs);
            target.setSubmissionCurrency(Currency.fromValue(source.getSubmissionCurrency()));
        }

        if (source.getSubmissionPeriod() != null) {
            String oldValue = Objects.nonNull(target.getSubmissionPeriod()) ? target.getSubmissionPeriod().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Submission.class, "setSubmissionPeriod", RatePeriod.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getSubmissionPeriod(),activityLogs);
            target.setSubmissionPeriod(RatePeriod.fromValue(source.getSubmissionPeriod()));
        }

        if (source.getOfferAmount() != null) {
            String oldValue = Objects.nonNull(target.getOfferAmount()) ? target.getOfferAmount().toString() : null;
            String field = getFieldName(Submission.class, "setOfferAmount", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getOfferAmount().toString(),activityLogs);
            target.setOfferAmount(source.getOfferAmount());
        }

        if (source.getOfferCurrency() != null) {
            String oldValue = Objects.nonNull(target.getOfferCurrency()) ? target.getOfferCurrency().name() : null;
            String field = getFieldName(Submission.class, "setOfferCurrency", com.troy.ats.enums.Currency.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getOfferCurrency(),activityLogs);
            target.setOfferCurrency(Currency.fromValue(source.getOfferCurrency()));
        }

        if (source.getOfferPeriod() != null) {
            String oldValue = Objects.nonNull(target.getOfferPeriod()) ? target.getOfferPeriod().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Submission.class, "setOfferPeriod", RatePeriod.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getOfferPeriod(),activityLogs);
            target.setOfferPeriod(RatePeriod.fromValue(source.getOfferPeriod()));
        }

        if(Objects.nonNull(source.getNotes())) {
            target.setNotes(source.getNotes());
        }

        source.setActivityLogs(activityLogs);
    }

}