package com.troy.ats.service.impl;

import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.repository.StatusRepository;
import com.troy.ats.repository.SubStatusRepository;
import com.troy.ats.service.SubmissionStatusService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("submissionStatusService")
public class SubmissionStatusServiceImpl implements SubmissionStatusService {


    private final StatusRepository statusRepository;
    private final SubStatusRepository subStatusRepository;

    public SubmissionStatusServiceImpl(StatusRepository statusRepository, SubStatusRepository subStatusRepository) {
        this.statusRepository = statusRepository;
        this.subStatusRepository = subStatusRepository;
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
    public Status createStatus(Status request) {
        return null;
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public Status UpdateStatus(Status request) {
        return null;
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
     * @param id
     * @return
     */
    @Override
    public SubStatus getSubStatusById(UUID id) {

        return subStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sub Status not found: " + id));
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
}