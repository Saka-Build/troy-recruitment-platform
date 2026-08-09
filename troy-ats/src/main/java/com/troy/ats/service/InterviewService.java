package com.troy.ats.service;

import com.troy.ats.entity.Interview;
import com.troy.ats.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;

    @Transactional(readOnly = true)
    public Page<Interview> getAllInterviews(Pageable pageable) {
        return interviewRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Interview> getInterviewById(UUID id) {
        return interviewRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByCandidateId(UUID candidateId) {
        return interviewRepository.findByCandidateId(candidateId);
    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByJobId(UUID jobId) {
        return interviewRepository.findByJobId(jobId);
    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByDate(LocalDate date) {
        return interviewRepository.findByInterviewDate(date);
    }

    @Transactional
    public Interview createInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    @Transactional
    public Interview updateInterview(UUID id, Interview interview) {
        interview.setId(id);
        return interviewRepository.save(interview);
    }

    @Transactional
    public void deleteInterview(UUID id) {
        interviewRepository.deleteById(id);
    }
}

