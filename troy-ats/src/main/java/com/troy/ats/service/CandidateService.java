package com.troy.ats.service;

import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Submission;
import com.troy.ats.repository.CandidateRepository;
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
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final SubmissionRepository submissionRepository;

    @Transactional(readOnly = true)
    public Page<Candidate> getAllCandidates(Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Candidate> getCandidateById(UUID id) {
        return candidateRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Candidate> getCandidateByCvId(String cvId) {
        return candidateRepository.findByCvId(cvId);
    }

    @Transactional
    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Transactional
    public Candidate updateCandidate(UUID id, Candidate candidate) {
        candidate.setId(id);
        return candidateRepository.save(candidate);
    }

    @Transactional
    public void deleteCandidate(UUID id) {
        candidateRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsForCandidate(UUID candidateId) {
        return submissionRepository.findByCandidateId(candidateId);
    }
}

