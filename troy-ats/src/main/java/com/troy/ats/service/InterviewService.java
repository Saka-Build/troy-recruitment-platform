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

public interface InterviewService {

    /**
     *
     * @return
     */
    public List<Interview> getAllInterviews();

    /**
     *
     * @param id
     * @return
     */
    public Optional<Interview> getInterviewById(Long id);

    /**
     *
     * @param date
     * @return
     */
    public List<Interview> getInterviewsByDate(LocalDate date);

    /**
     *
     * @param interview
     * @return
     */
    public Interview createInterview(Interview interview);

    /**
     *
     * @param id
     */
    public void deleteInterview(Long id);

    /**
     *
     * @param zoneId
     * @return
     */
    public List<Interview> getTodayInterviewsForZoneIdWithDescOrder(ZoneId zoneId);

    /**
     *
     * @return
     */
    public long getTotalClientFeedBackPending();
}