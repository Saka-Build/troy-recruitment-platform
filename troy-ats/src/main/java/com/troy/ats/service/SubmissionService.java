package com.troy.ats.service;

import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    @Transactional(readOnly = true)
    public Page<Submission> getAllSubmissions(Pageable pageable) {
        return submissionRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Submission> getSubmissionById(UUID id) {
        return submissionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Submission> getSubmissionByCandidateAndJob(UUID candidateId, UUID jobId) {
        return submissionRepository.findByCandidateIdAndJobId(candidateId, jobId);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsByCandidateId(UUID candidateId) {
        return submissionRepository.findByCandidateId(candidateId);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsByJobId(UUID jobId) {
        return submissionRepository.findByJobId(jobId);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsByPipelineStage(PipelineStage stage) {
        return submissionRepository.findByPipelineStage(stage);
    }

    @Transactional
    public Submission createSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    @Transactional
    public Submission updateSubmission(UUID id, Submission submission) {
        submission.setId(id);
        return submissionRepository.save(submission);
    }

    @Transactional
    public void deleteSubmission(UUID id) {
        submissionRepository.deleteById(id);
    }
}

