package com.troy.ats.service;

import com.troy.ats.entity.Interview;
import com.troy.ats.enums.InterviewOutcome;
import com.troy.ats.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;

    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    public Optional<Interview> getInterviewById(Long id) {
        return interviewRepository.findById(id);
    }

    public List<Interview> getInterviewsByDate(LocalDate date) {
        return interviewRepository.findByInterviewDate(date);
    }

    public Interview createInterview(Interview interview) {
        return interviewRepository.save(interview);
    }

    public void deleteInterview(Long id) {
        interviewRepository.deleteById(id);
    }

    public List<Interview> getTodayInterviewsForZoneIdWithDescOrder(ZoneId zoneId) {

        LocalDate today = LocalDate.now(zoneId);
        Instant start = today.atStartOfDay(zoneId).toInstant();
        Instant end = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        return interviewRepository.findByInterviewDateTimeWithZoneGreaterThanEqualAndInterviewDateTimeWithZoneLessThanOrderByInterviewDateTimeWithZoneDesc(start, end);
    }

    public long getTotalClientFeedBackPending(){
        return interviewRepository.countByOutcomeAndFeedbackEmpty(InterviewOutcome.completed);
    }
}