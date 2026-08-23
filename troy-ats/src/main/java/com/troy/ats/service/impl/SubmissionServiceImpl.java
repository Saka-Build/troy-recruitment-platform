package com.troy.ats.service.impl;

import com.troy.ats.dto.*;
import com.troy.ats.entity.ActivityLog;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.populator.CandidatePipelinePopulator;
import com.troy.ats.populator.ReverseSubmissionPopulator;
import com.troy.ats.populator.SubmissionPopulator;
import com.troy.ats.repository.SubmissionRepository;
import com.troy.ats.searchfilter.dto.SubmissionFilter;
import com.troy.ats.searchfilter.filter.SubmissionSpecification;
import com.troy.ats.service.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.troy.ats.constants.CommonConstants.STATUS_APPLIED;
import static com.troy.ats.constants.CommonConstants.SUBSTATUS_READY_FOR_SUBMISSION;
import static com.troy.ats.util.CommonUtil.logActivity;

@Slf4j
@Service("submissionService")
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final CandidatePipelinePopulator candidatePipelinePopulator;
    private final ReverseSubmissionPopulator reverseSubmissionPopulator;
    private final SubmissionStatusServiceImpl submissionStatusService;
    private final SessionServiceImpl sessionService;
    private final SubmissionPopulator submissionPopulator;
    private final ActivityLogServiceImpl activityLogService;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository, CandidatePipelinePopulator candidatePipelinePopulator, ReverseSubmissionPopulator reverseSubmissionPopulator, SubmissionStatusServiceImpl submissionStatusService, SessionServiceImpl sessionService, SubmissionPopulator submissionPopulator, ActivityLogServiceImpl activityLogService) {
        this.submissionRepository = submissionRepository;
        this.candidatePipelinePopulator = candidatePipelinePopulator;
        this.reverseSubmissionPopulator = reverseSubmissionPopulator;
        this.submissionStatusService = submissionStatusService;
        this.sessionService = sessionService;
        this.submissionPopulator = submissionPopulator;
        this.activityLogService = activityLogService;
    }

    @Override
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    @Override
    public Submission getSubmissionById(UUID id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found: " + id));
    }

    @Override
    @Transactional
    public SubmissionDto createSubmission(SubmissionCreateRequest request) {
        Submission submission = new Submission();
        reverseSubmissionPopulator.populate(request, submission);
        submission.setPipelineStage(PipelineStage.APPLIED);

        Status status = submissionStatusService.getStatusByName(STATUS_APPLIED);
        SubStatus subStatus = submissionStatusService.getSUbStatusByName(SUBSTATUS_READY_FOR_SUBMISSION);

        submission.setStatus(status);
        submission.setSubStatus(subStatus);
        submission.setSubmittedBy(sessionService.getCurrentUser());
        submission.setSubmittedAt(Instant.now());

        submission = submissionRepository.save(submission);

        ActivityLogRequest activityLogRequest = new ActivityLogRequest();
        activityLogRequest.setEntityType( submission.getClass().getSimpleName().toLowerCase(Locale.ROOT));
        activityLogRequest.setEntityId(submission.getId());
        List<ActivityLog> logs = logActivity(List.of(activityLogRequest), sessionService,false);
        activityLogService.saveAll(logs);

        SubmissionDto submissionDto = new SubmissionDto();
        submissionPopulator.populate(submission, submissionDto);
        return submissionDto;

    }

    /**
     *
     * @param submissionId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public SubmissionDto updateSubmission(UUID submissionId, SubmissionCreateRequest request) {

        Submission submission = getSubmissionById(submissionId);
        reverseSubmissionPopulator.populate(request, submission);

        // Save submission first
        submission = submissionRepository.save(submission);

        List<ActivityLog> logs = logActivity(request.getActivityLogs(), sessionService,true);
        activityLogService.saveAll(logs);

        SubmissionDto submissionDto = new SubmissionDto();
        submissionPopulator.populate(submission, submissionDto);

        return submissionDto;
    }

    @Override
    public void deleteSubmission(UUID id) {
        submissionRepository.deleteById(id);
    }

    @Override
    public long getTotalCVSubmissionsByPipelineStage(PipelineStage pipelineStage) {
        return submissionRepository.countByPipelineStage(pipelineStage);
    }

    @Override
    public List<PipelineDto> getCandidatePipelines() {

        List<PipelineStage> stages = List.of(
                PipelineStage.APPLIED,
                PipelineStage.SCREENING,
                PipelineStage.READY_TO_SUBMIT,
                PipelineStage.SUBMITTED,
                PipelineStage.INTERVIEW,
                PipelineStage.OFFER
        );

        List<Submission> submissions = submissionRepository.findByPipelineStageIn(stages);

        Map<PipelineStage, List<Submission>> groupedSubmissions = submissions.stream().collect(Collectors.groupingBy(Submission::getPipelineStage));

        List<PipelineDto> pipelineDtoList = stages.stream().map(stage -> {
            PipelineDto pipelineDto = new PipelineDto();
            List<Submission> submissionList = groupedSubmissions.getOrDefault(stage, List.of());
            pipelineDto.setPipelineStage(stage);
            pipelineDto.setTotalCandidates(submissionList.size());
            List<CandidatePipelineDto> CandidatePipelineDtoList = submissionList.stream()
                    .map(submission -> {
                        CandidatePipelineDto candidate = new CandidatePipelineDto();
                        candidatePipelinePopulator.populate(submission, candidate);
                        return candidate;
                    }).toList();
            pipelineDto.setCandidates(CandidatePipelineDtoList);
            return pipelineDto;

        }).toList();

        return pipelineDtoList;

    }

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubmissionDto> getSubmissions(SubmissionFilter filter, Pageable pageable) {

        return submissionRepository.findAll(SubmissionSpecification.filter(filter), pageable)
                .map(submission -> {
                    SubmissionDto dto = new SubmissionDto();
                    submissionPopulator.populate(submission, dto);
                    return dto;
                });
    }

    /**
     *
     * @param pipelineStage
     * @return
     */
    @Override
    public List<String> findJobNamesByPipelineStage(String pipelineStage) {

        PipelineStage pipeline = PipelineStage.fromValue(pipelineStage);
        List<String> jobNames = submissionRepository.findJobTitlesByPipelineStage(pipeline);
        return jobNames;
    }

    /**
     *
     * @return
     */
    @Override
    public CountSubmissionsByPipelineStageDto submissionCountsByPipelines() {

        CountSubmissionsByPipelineStageDto dto = new CountSubmissionsByPipelineStageDto();
        dto.setTotalApplied(getTotalCVSubmissionsByPipelineStage(PipelineStage.APPLIED));
        dto.setTotalScreening(getTotalCVSubmissionsByPipelineStage(PipelineStage.SCREENING));
        dto.setTotalReadyToSubmit(getTotalCVSubmissionsByPipelineStage(PipelineStage.READY_TO_SUBMIT));
        dto.setTotalSubmitted(getTotalCVSubmissionsByPipelineStage(PipelineStage.SUBMITTED));
        dto.setTotalInterview(getTotalCVSubmissionsByPipelineStage(PipelineStage.INTERVIEW));
        dto.setTotalOffer(getTotalCVSubmissionsByPipelineStage(PipelineStage.OFFER));
        dto.setTotalJoined(getTotalCVSubmissionsByPipelineStage(PipelineStage.JOINED));

        return dto;

    }

}