package com.troy.ats.service;

import com.troy.ats.dto.InterviewDto;
import com.troy.ats.dto.InterviewScheduleRequest;
import com.troy.ats.entity.Interview;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Interview getInterviewById(UUID id);

    /**
     *
     * @param id
     * @return
     */
    public InterviewDto getInterviewDtoById(UUID id);

    /**
     *
     * @param id
     * @return
     */
    public List<InterviewDto> getInterviewsBySubmissionId(UUID id);

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
    public InterviewDto createInterview(InterviewScheduleRequest request);

    /**
     *
     * @param request
     * @return
     */
    public InterviewDto updateInterview(UUID interviewId, InterviewScheduleRequest request);

    /**
     *
     * @param id
     */
    public void deleteInterview(UUID id);

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