package com.troy.ats.service;

import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Status;
import com.troy.ats.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidateById(UUID id) {
        return candidateRepository.findById(id);
    }

    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(UUID id, Candidate candidate) {
       // candidate.setId(id);
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(UUID id) {
        candidateRepository.deleteById(id);
    }

    public long getTotalCandidatesByStatus(boolean active) {
        return candidateRepository.countByStatus_Active(active);
    }
    public long getTotalCandidatesByStatusName(String statusName) {
        return candidateRepository.countByStatus_Name(statusName);
    }
    public List<Candidate> getCandidatesByStatusNameANDSubStatusName(String statusName, String subStatusName) {
        return candidateRepository.findByStatus_NameAndSubStatus_Name(statusName, subStatusName);
    }
}

