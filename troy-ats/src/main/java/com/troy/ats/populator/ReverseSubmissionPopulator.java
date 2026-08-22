package com.troy.ats.populator;

import com.troy.ats.dto.SubmissionCreateRequest;
import com.troy.ats.entity.*;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.repository.StatusRepository;
import com.troy.ats.service.CandidateService;
import com.troy.ats.service.impl.JobServiceImpl;
import com.troy.ats.service.impl.SubmissionStatusServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ReverseSubmissionPopulator {


    private final CandidateService candidateService;
    private final JobServiceImpl jobService;
    private final StatusRepository statusRepository;
    private final SubmissionStatusServiceImpl submissionStatusService;

    public void populate(SubmissionCreateRequest source, Submission target) {

        if(Objects.nonNull(source.getCandidateId())){
            Candidate candidate = candidateService.getCandidateById(source.getCandidateId());
            target.setCandidate(candidate);
        }

        if(Objects.nonNull(source.getJobId())) {
            Job job = jobService.getJobById(source.getJobId());
            target.setJob(job);
        }
        if(Objects.nonNull(source.getPipelineStage())) {
            target.setPipelineStage(PipelineStage.fromValue(source.getPipelineStage()));
        }
        if(Objects.nonNull(source.getStatusId())) {
            Status status = submissionStatusService.getStatusById(source.getStatusId());
            target.setStatus(status);
        }
        if(Objects.nonNull(source.getSubStatusId())) {
            SubStatus ubStatus = submissionStatusService.getSubStatusById(source.getSubStatusId());
            target.setSubStatus(ubStatus);
        }

        if(Objects.nonNull(source.getNotes())) {
            target.setNotes(source.getNotes());
        }
    }


}