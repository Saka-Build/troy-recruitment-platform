package com.troy.ats.service;

import com.troy.ats.dto.SubmissionStatus;
import com.troy.ats.dto.SubmissionStatusRequest;
import com.troy.ats.dto.SubmissionStatusesDto;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;

import java.util.List;
import java.util.UUID;

public interface SubmissionStatusService {

    /**
     *
     * @return
     */
    public List<Status> getAllStatus();

    /**
     *
     * @return
     */
    public List<SubmissionStatus> getAllActiveStatus();

    /**
     *
     * @param id
     * @return
     */
    public Status getStatusById(UUID id);

    /**
     *
     * @param id
     * @return
     */
    public SubmissionStatus getStatusDtoById(UUID id);

    /**
     *
     * @param name
     * @return
     */
    public Status getStatusByName(String name);

    /**
     *
     * @param request
     * @return
     */
    public Status createStatus(SubmissionStatusRequest request);

    /**
     *
     * @param request
     * @return
     */
    public Status UpdateStatus(UUID id, SubmissionStatusRequest request);

    /**
     *
     * @param id
     */
    public void deleteStatus(UUID id) ;

    /**
     *
     * @return
     */
    public List<SubStatus> getAllSubStatus();

    /**
     *
     * @return
     */
    public List<SubmissionStatus> getAllActiveSubStatus();

    /**
     *
     * @param id
     * @return
     */
    public SubStatus getSubStatusById(UUID id);

    public SubmissionStatus getSubStatusDtoById(UUID id);

    /**
     *
     * @param name
     * @return
     */
    public SubStatus getSUbStatusByName(String name);

    /**
     *
     * @param request
     * @return
     */
    public SubStatus createSubStatus(SubmissionStatusRequest request);

    /**
     *
     * @param request
     * @return
     */
    public SubmissionStatus UpdateSubStatus(UUID id, SubmissionStatusRequest request);

    /**
     *
     * @param id
     */
    public void deleteSubStatus(UUID id) ;

    /**
     *
     * @param id
     * @return
     */
    public List<SubmissionStatus> getSubstatusesForStatusId(UUID id);

    /**
     *
     * @return
     */
    SubmissionStatusesDto getSubmissionStatuses();

}