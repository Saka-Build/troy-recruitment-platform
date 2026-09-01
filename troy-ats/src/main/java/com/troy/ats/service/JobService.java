package com.troy.ats.service;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.searchfilter.dto.JobExportFilter;
import com.troy.ats.searchfilter.dto.JobFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
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

    /**
     *
     * @param jobId
     * @param request
     * @return
     */
    JobDto updateJob(UUID jobId, JobCreateRequest request);

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    Page<JobDto> getJobs(JobFilter filter, Pageable pageable);

    /**
     *
     * @param id
     * @return
     */
    JobDto getJobDtoById(UUID id);

    /**
     *
     * @param filter
     * @return
     * @throws IOException
     */
    byte[] exportJobs(JobExportFilter filter) throws IOException;

    /**
     *
     * @param clientName
     * @param endClientName
     * @param number
     * @return
     */
    String generateJobId(String clientName, String endClientName);

    /**
     *
     * @return
     */
    JobsFiltersDto getJobFilters();

    /**
     *
     * @param statuses
     * @return
     */
    List<Job> findByStatusIn(List<JobStatus> statuses);
}