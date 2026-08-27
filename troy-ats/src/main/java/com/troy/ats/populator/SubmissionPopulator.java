package com.troy.ats.populator;

import com.troy.ats.dto.SubmissionDto;
import com.troy.ats.entity.Submission;
import com.troy.ats.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

import static com.troy.ats.util.CommonUtil.enumToStringFormat;


@Component
@RequiredArgsConstructor
public class SubmissionPopulator {

    private final ActivityLogService activityLogService;

    public void populate(Submission source, SubmissionDto target) {

        target.setSubmissionId(source.getId());

        target.setCandidateId(source.getCandidate().getId());
        target.setCandidateCVId(source.getCandidate().getCvId());
        target.setCandidateName(source.getCandidate().getFullName());
        target.setCandidateDesignation(source.getCandidate().getCurrentDesignation());

        target.setJobId(source.getJob().getId());
        target.setJobName(source.getJob().getTitle());
        target.setClientName(source.getJob().getClient().getName());

        target.setPipelineStage(enumToStringFormat(source.getPipelineStage().name()));

        if(Objects.nonNull(source.getStatus())){
            target.setStatusId(source.getStatus().getId());
            target.setStatusName(source.getStatus().getName());
        }

        if(Objects.nonNull(source.getSubStatus())){
            target.setSubStatusId(source.getSubStatus().getId());
            target.setSubStatusName(source.getSubStatus().getName());
        }
        target.setCandidateExpectedAmount(source.getCandidateExpectedAmount());
        if(source.getCandidateExpectedCurrency() != null){
            target.setCandidateExpectedCurrency(source.getCandidateExpectedCurrency().name());
        }
        if(source.getCandidateExpectedPeriod() != null){
            target.setCandidateExpectedPeriod(source.getCandidateExpectedPeriod().name().toLowerCase(Locale.ROOT));
        }

        target.setSubmissionAmount(source.getSubmissionAmount());
        if(source.getSubmissionCurrency() != null){
            target.setSubmissionCurrency(source.getSubmissionCurrency().name());
        }
        if(source.getSubmissionPeriod() != null){
            target.setSubmissionPeriod(source.getSubmissionPeriod().name().toLowerCase(Locale.ROOT));
        }

        target.setOfferAmount(source.getOfferAmount());
        if(source.getOfferCurrency() != null){
            target.setOfferCurrency(source.getOfferCurrency().name());
        }
        if(source.getOfferPeriod() != null){
            target.setOfferPeriod(source.getOfferPeriod().name().toLowerCase(Locale.ROOT));
        }

        target.setNotes(source.getNotes());

        String entityType = source.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        target.setHistoryCounts(activityLogService.countByEntityTypeAndEntityId(entityType, source.getId()));
    }

}