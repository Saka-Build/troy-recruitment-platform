package com.troy.ats.service.impl;

import com.troy.ats.dto.JobCreateRequest;
import com.troy.ats.dto.JobDto;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.populator.JobPopulator;
import com.troy.ats.populator.ReverseJobPopulator;
import com.troy.ats.repository.JobRepository;
import com.troy.ats.service.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("jobService")
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ReverseJobPopulator reverseJobPopulator;
    private final JobPopulator jobPopulator;

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + id));
    }

    @Override
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, Job job) {
       // job.setId(id);
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(UUID id) {
        jobRepository.deleteById(id);
    }

    @Override
    public long getTotalJobsByStatus(JobStatus status) {
        return jobRepository.countByStatus(status);
    }

    @Override
    public long getTotalJobsByPriority(String priority) {
        return jobRepository.countByPriority(priority);
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public JobDto createJob(JobCreateRequest request) {

        validateJobRequest(request);
        Job job = new Job();
        reverseJobPopulator.populate(request,job);
        Job savedJob = jobRepository.save(job);

        // 8. Convert to DTO
        JobDto jobDto = new JobDto();
        jobPopulator.populate(savedJob, jobDto);

        return jobDto;
    }

    private void validateJobRequest(JobCreateRequest request){
        // 4. Validate experience
        if (request.getExperienceMin() != null && request.getExperienceMax() != null
                && request.getExperienceMin().compareTo(request.getExperienceMax()) > 0) {

            throw new IllegalArgumentException("Minimum experience cannot be greater than maximum experience");
        }

        // 5. Validate salary
        if (request.getSalaryMin() != null && request.getSalaryMax() != null
                && request.getSalaryMin().compareTo(request.getSalaryMax()) > 0) {

            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
        }
    }

}