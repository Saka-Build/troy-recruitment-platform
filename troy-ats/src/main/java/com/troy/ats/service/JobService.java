package com.troy.ats.service;

import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(UUID id) {
        return jobRepository.findById(id);
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, Job job) {
       // job.setId(id);
        return jobRepository.save(job);
    }

    public void deleteJob(UUID id) {
        jobRepository.deleteById(id);
    }

    public long getTotalJobsByStatus(JobStatus status) {
        return jobRepository.countByStatus(status);
    }

    public long getTotalJobsByPriority(String priority) {
        return jobRepository.countByPriority(priority);
    }
}