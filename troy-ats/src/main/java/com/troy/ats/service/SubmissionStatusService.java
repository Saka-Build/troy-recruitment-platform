package com.troy.ats.service;

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
     * @param id
     * @return
     */
    public Status getStatusById(UUID id);

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
    public Status createStatus(Status request);

    /**
     *
     * @param request
     * @return
     */
    public Status UpdateStatus(Status request);

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
     * @param id
     * @return
     */
    public SubStatus getSubStatusById(UUID id);

    /**
     *
     * @param name
     * @return
     */
    public SubStatus getSUbStatusByName(String name);



}