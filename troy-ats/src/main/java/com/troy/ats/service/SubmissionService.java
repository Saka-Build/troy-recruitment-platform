package com.troy.ats.service;

import com.troy.ats.entity.Submission;
import com.troy.ats.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Optional<Submission> getSubmissionById(UUID id) {
        return submissionRepository.findById(id);
    }

    public Submission createSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    public void deleteSubmission(UUID id) {
        submissionRepository.deleteById(id);
    }
}