package com.troy.ats.service.impl;

import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.dto.InterviewDto;
import com.troy.ats.dto.InterviewScheduleRequest;
import com.troy.ats.entity.ActivityLog;
import com.troy.ats.entity.Interview;
import com.troy.ats.entity.Submission;
import com.troy.ats.enums.InterviewOutcome;
import com.troy.ats.populator.InterviewPopulator;
import com.troy.ats.populator.ReverseInterviewPopulator;
import com.troy.ats.repository.InterviewRepository;
import com.troy.ats.service.ActivityLogService;
import com.troy.ats.service.InterviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.troy.ats.util.CommonUtil.logActivity;

@Service("interviewService")
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ReverseInterviewPopulator reverseInterviewPopulator;
    private final InterviewPopulator interviewPopulator;
    private final SessionServiceImpl sessionService;
    private final ActivityLogService activityLogService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    @Override
    public List<Interview> getAllInterviews() {
        return interviewRepository.findAll();
    }

    @Override
    public Interview getInterviewById(UUID id) {

        return interviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interview not found: " + id));
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public InterviewDto getInterviewDtoById(UUID id) {
        Interview interview = getInterviewById(id);

        InterviewDto interviewDto = new InterviewDto();
        interviewPopulator.populate(interview, interviewDto);
        return interviewDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewDto> getInterviewsBySubmissionId(UUID id) {
        return interviewRepository.findBySubmissionId(id).stream()
                .map(interview -> {
                    InterviewDto dto = new InterviewDto();
                    interviewPopulator.populate(interview, dto);
                    return dto;
                }).toList();
    }

    @Override
    public List<Interview> getInterviewsByDate(LocalDate date) {
        return interviewRepository.findByInterviewDate(date);
    }

    @Override
    @Transactional
    public InterviewDto createInterview(InterviewScheduleRequest request) {

        Interview interview = new Interview();
        reverseInterviewPopulator.populate(request, interview, sessionService);
        interview.setOutcome(InterviewOutcome.scheduled);

        interview = interviewRepository.save(interview);

        ActivityLogRequest activityLogRequest = new ActivityLogRequest();
        activityLogRequest.setEntityType( Submission.class.getSimpleName().toLowerCase(Locale.ROOT));
        activityLogRequest.setEntityId(request.getSubmissionId());
        activityLogRequest.setAction("Interview Scheduled");
        List<ActivityLog> logs = logActivity(List.of(activityLogRequest), sessionService,false);
        activityLogService.saveAll(logs);

        InterviewDto dto = new InterviewDto();
        interviewPopulator.populate(interview, dto);

        return dto;

    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public InterviewDto updateInterview(UUID interviewId, InterviewScheduleRequest request) {

        Interview interview = getInterviewById(interviewId);
        reverseInterviewPopulator.populate(request, interview, sessionService);

        interview = interviewRepository.save(interview);

        List<ActivityLog> logs = logActivity(request.getActivityLogs(), sessionService,true);
        activityLogService.saveAll(logs);


        InterviewDto dto = new InterviewDto();
        interviewPopulator.populate(interview, dto);

        return dto;
    }

    @Override
    public void deleteInterview(UUID id) {

        interviewRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
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