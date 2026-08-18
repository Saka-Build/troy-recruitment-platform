package com.troy.ats.service;

import com.troy.ats.dto.JobCreateRequest;
import com.troy.ats.dto.JobDto;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobService {

    /**
     *
     * @return
     */
    public List<Job> getAllJobs();

    /**
     *
     * @param id
     * @return
     */
    public Job getJobById(UUID id);

    /**
     *
     * @param job
     * @return
     */
    public Job createJob(Job job);

    /**
     *
     * @param id
     * @param job
     * @return
     */

    /**
     *
     * @param id
     * @param job
     * @return
     */
    public Job updateJob(Long id, Job job);

    /**
     *
     * @param id
     */
    public void deleteJob(UUID id);

    /**
     *
     * @param status
     * @return
     */
    public long getTotalJobsByStatus(JobStatus status);

    /**
     *
     * @param priority
     * @return
     */
    public long getTotalJobsByPriority(String priority);

    /**
     *
     * @param request
     * @return
     */
    JobDto createJob(JobCreateRequest request);
}