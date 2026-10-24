package com.troy.ats.service.impl;

import com.troy.ats.dto.SubmissionStatus;
import com.troy.ats.dto.SubmissionStatusRequest;
import com.troy.ats.dto.SubmissionStatusesDto;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.populator.ReverseSubmissionStatusPopulator;
import com.troy.ats.populator.SubmissionStatusPopulator;
import com.troy.ats.repository.StatusRepository;
import com.troy.ats.repository.SubStatusRepository;
import com.troy.ats.service.SubmissionStatusService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.troy.ats.util.CommonUtil.enumToStringFormat;

@Service("submissionStatusService")
public class SubmissionStatusServiceImpl implements SubmissionStatusService {


    private final StatusRepository statusRepository;
    private final SubStatusRepository subStatusRepository;
    private final ReverseSubmissionStatusPopulator reverseSubmissionStatusPopulator;
    private final SubmissionStatusPopulator submissionStatusPopulator;

    public SubmissionStatusServiceImpl(StatusRepository statusRepository, SubStatusRepository subStatusRepository, ReverseSubmissionStatusPopulator reverseSubmissionStatusPopulator, SubmissionStatusPopulator submissionStatusPopulator) {
        this.statusRepository = statusRepository;
        this.subStatusRepository = subStatusRepository;
        this.reverseSubmissionStatusPopulator = reverseSubmissionStatusPopulator;
        this.submissionStatusPopulator = submissionStatusPopulator;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Status> getAllStatus() {

        return statusRepository.findAll();

    }

    /**
     *
     * @return
     */
    @Override
    public List<SubmissionStatus> getAllActiveStatus() {

        List<Status> statuses = statusRepository.findByActiveTrue();

        return statuses.stream().map(status -> {
            SubmissionStatus submissionStatus = new SubmissionStatus();
            submissionStatusPopulator.populate(status, null, submissionStatus);
            return submissionStatus;
        }).toList();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Status getStatusById(UUID id) {

        return statusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status not found: " + id));
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public SubmissionStatus getStatusDtoById(UUID id) {

        Status status = getStatusById(id);
        SubmissionStatus submissionStatus = new SubmissionStatus();
        submissionStatusPopulator.populate(status,null,submissionStatus);
        return submissionStatus;
    }

    /**
     *
     * @param name
     * @return
     */
    @Override
    public Status getStatusByName(String name) {
        return statusRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Status not found: " + name));
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public Status createStatus(SubmissionStatusRequest request) {

        Status status = new Status();
        reverseSubmissionStatusPopulator.populate(request, status, null);
        status = statusRepository.save(status);
        return status;
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public Status UpdateStatus(UUID id, SubmissionStatusRequest request) {
        Status status = getStatusById(id);
        reverseSubmissionStatusPopulator.populate(request, status, null);
        status = statusRepository.save(status);
        return status;
    }

    /**
     *
     * @param id
     */
    @Override
    public void deleteStatus(UUID id) {
        statusRepository.deleteById(id);
    }

    /**
     *
     * @return
     */
    @Override
    public List<SubStatus> getAllSubStatus() {
        return subStatusRepository.findAll();
    }

    /**
     *
     * @return
     */
    @Override
    public List<SubmissionStatus> getAllActiveSubStatus() {

        List<SubStatus> substauses = subStatusRepository.findByActiveTrue();

        return substauses.stream().map(subStatus -> {
            SubmissionStatus submissionStatus = new SubmissionStatus();
            submissionStatusPopulator.populate(null, subStatus, submissionStatus);
            return submissionStatus;
        }).toList();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public SubStatus getSubStatusById(UUID id) {

        return subStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sub Status not found: " + id));
    }

    @Override
    public SubmissionStatus getSubStatusDtoById(UUID id) {
        SubStatus subStatus = getSubStatusById(id);
        SubmissionStatus submissionStatus = new SubmissionStatus();
        submissionStatusPopulator.populate(null,subStatus,submissionStatus);
        return submissionStatus;
    }

    /**
     *
     * @param name
     * @return
     */
    @Override
    public SubStatus getSUbStatusByName(String name) {
        return subStatusRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Sub Status not found: " + name));
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public SubStatus createSubStatus(SubmissionStatusRequest request) {
        SubStatus subStatus = new SubStatus();
        reverseSubmissionStatusPopulator.populate(request, null, subStatus);
        Status status = getStatusById(request.getStatusId());
        subStatus.setStatus(status);
        subStatus = subStatusRepository.save(subStatus);
        return subStatus;
    }

    /**
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    @Transactional
    public SubmissionStatus UpdateSubStatus(UUID id, SubmissionStatusRequest request) {
        SubStatus subStatus = getSubStatusById(id);
        reverseSubmissionStatusPopulator.populate(request, null, subStatus);
        if(Objects.nonNull(request.getStatusId())){
            Status status = getStatusById(request.getStatusId());
            subStatus.setStatus(status);
        }
        subStatus = subStatusRepository.save(subStatus);
        SubmissionStatus submissionStatus = new SubmissionStatus();
        submissionStatusPopulator.populate(null, subStatus, submissionStatus);
        return submissionStatus;
    }

    /**
     *
     * @param id
     */
    @Override
    public void deleteSubStatus(UUID id) {
        subStatusRepository.deleteById(id);
    }

    @Override
    public List<SubmissionStatus> getSubstatusesForStatusId(UUID id) {

        List<SubStatus> substauses = subStatusRepository.findByStatusIdAndActiveTrue(id);
        return substauses.stream().map(subStatus -> {
                                    SubmissionStatus submissionStatus = new SubmissionStatus();
                                    submissionStatusPopulator.populate(null, subStatus, submissionStatus);
                                    return submissionStatus;
                                }).toList();
    }

    /**
     *
     * @return
     */
    @Override
    public SubmissionStatusesDto getSubmissionStatuses() {

        List<SubmissionStatus> submissionStatuses = getAllActiveStatus();

        List<String> stages = List.of(
                enumToStringFormat(PipelineStage.APPLIED.name()),
                enumToStringFormat(PipelineStage.SCREENING.name()),
                enumToStringFormat(PipelineStage.READY_TO_SUBMIT.name()),
                enumToStringFormat(PipelineStage.SUBMITTED.name()),
                enumToStringFormat(PipelineStage.INTERVIEW.name()),
                enumToStringFormat(PipelineStage.SELECTED.name()),
                enumToStringFormat(PipelineStage.REJECTED.name()),
                enumToStringFormat(PipelineStage.ONBOARDING.name()),
                enumToStringFormat(PipelineStage.ONBOARDED.name())
        );
        SubmissionStatusesDto submissionStatusesDto = new SubmissionStatusesDto();
        submissionStatusesDto.setSubmissionStatusList(submissionStatuses);
        submissionStatusesDto.setWorkflowStages(stages);

        return submissionStatusesDto;

    }
}