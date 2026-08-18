package com.troy.ats.service.impl;

import com.troy.ats.entity.Interview;
import com.troy.ats.enums.InterviewOutcome;
import com.troy.ats.repository.InterviewRepository;
import com.troy.ats.service.InterviewService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service("interviewService")
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewServiceImpl(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    @Override
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    @Override
    public Optional<Interview> getInterviewById(Long id) {
        return interviewRepository.findById(id);
    }

    @Override
    public List<Interview> getInterviewsByDate(LocalDate date) {
        return interviewRepository.findByInterviewDate(date);
    }

    @Override
    public Interview createInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    @Override
    public void deleteInterview(Long id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public List<Interview> getTodayInterviewsForZoneIdWithDescOrder(ZoneId zoneId) {

        LocalDate today = LocalDate.now(zoneId);
        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return interviewRepository.findByInterviewDateTimeWithZoneGreaterThanEqualAndInterviewDateTimeWithZoneLessThanOrderByInterviewDateTimeWithZoneDesc(start, end);
    }

    @Override
    public long getTotalClientFeedBackPending(){
        return interviewRepository.countByOutcomeAndFeedbackEmpty(InterviewOutcome.completed);
    }
}