package com.troy.ats.service;

import com.troy.ats.dto.CandidatePipelineDto;
import com.troy.ats.dto.PipelineDto;
import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.populator.CandidatePipelinePopulator;
import com.troy.ats.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

public interface SubmissionService {

    /**
     *
     * @return
     */
    public List<Submission> getAllSubmissions();

    /**
     *
     * @param id
     * @return
     */
    public Submission getSubmissionById(UUID id);

    /**
     *
     * @param submission
     * @return
     */
    public Submission createSubmission(Submission submission);

    /**
     *
     * @param id
     */
    public void deleteSubmission(UUID id) ;

    /**
     *
     * @param pipelineStage
     * @return
     */
    public long getTotalCVSubmissionsByPipelineStage(PipelineStage pipelineStage);

    /**
     *
     * @return
     */
    public List<PipelineDto> getCandidatePipelines();

}