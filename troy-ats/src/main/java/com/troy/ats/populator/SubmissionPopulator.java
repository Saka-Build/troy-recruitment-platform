package com.troy.ats.populator;

import com.troy.ats.dto.SubmissionDto;
import com.troy.ats.entity.Submission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.troy.ats.util.CommonUtil.enumToStringFormat;


@Component
@RequiredArgsConstructor
public class SubmissionPopulator {


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

        target.setStatusId(source.getStatus().getId());
        target.setStatusName(source.getStatus().getName());

        target.setSubStatusId(source.getSubStatus().getId());
        target.setSubStatusName(source.getSubStatus().getName());

        target.setNotes(source.getNotes());
    }

}