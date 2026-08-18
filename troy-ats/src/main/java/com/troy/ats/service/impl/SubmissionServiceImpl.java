package com.troy.ats.service.impl;

import com.troy.ats.dto.CandidatePipelineDto;
import com.troy.ats.dto.PipelineDto;
import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.populator.CandidatePipelinePopulator;
import com.troy.ats.repository.SubmissionRepository;
import com.troy.ats.service.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("submissionService")
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final CandidatePipelinePopulator candidatePipelinePopulator;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository, CandidatePipelinePopulator candidatePipelinePopulator) {
        this.submissionRepository = submissionRepository;
        this.candidatePipelinePopulator = candidatePipelinePopulator;
    }

    @Override
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    @Override
    public Submission getSubmissionById(UUID id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found: " + id));
    }

    @Override
    public Submission createSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    @Override
    public void deleteSubmission(UUID id) {
        submissionRepository.deleteById(id);
    }

    @Override
    public long getTotalCVSubmissionsByPipelineStage(PipelineStage pipelineStage) {
        return submissionRepository.countByPipelineStage(pipelineStage);
    }

    @Override
    public List<PipelineDto> getCandidatePipelines() {

        List<PipelineStage> stages = List.of(
                PipelineStage.APPLIED,
                PipelineStage.SCREENING,
                PipelineStage.READY_TO_SUBMIT,
                PipelineStage.SUBMITTED,
                PipelineStage.INTERVIEW,
                PipelineStage.OFFER
        );

        List<Submission> submissions = submissionRepository.findByPipelineStageIn(stages);

        Map<PipelineStage, List<Submission>> groupedSubmissions = submissions.stream().collect(Collectors.groupingBy(Submission::getPipelineStage));

        List<PipelineDto> pipelineDtoList = stages.stream().map(stage -> {
            PipelineDto pipelineDto = new PipelineDto();
            List<Submission> submissionList = groupedSubmissions.getOrDefault(stage, List.of());
            pipelineDto.setPipelineStage(stage);
            pipelineDto.setTotalCandidates(submissionList.size());
            List<CandidatePipelineDto> CandidatePipelineDtoList = submissionList.stream()
                    .map(submission -> {
                        CandidatePipelineDto candidate = new CandidatePipelineDto();
                        candidatePipelinePopulator.populate(submission, candidate);
                        return candidate;
                    }).toList();
            pipelineDto.setCandidates(CandidatePipelineDtoList);
            return pipelineDto;

        }).toList();

        return pipelineDtoList;

    }

}