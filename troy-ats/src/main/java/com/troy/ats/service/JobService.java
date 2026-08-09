package com.troy.ats.service;

import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    @Transactional(readOnly = true)
    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Job> getJobById(UUID id) {
        return jobRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Job> getJobsByStatus(JobStatus status) {
        return jobRepository.findByStatus(status);
    }

    @Transactional
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    @Transactional
    public Job updateJob(UUID id, Job job) {
        job.setId(id);
        return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(UUID id) {
        jobRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Job> getJobsByClientId(UUID clientId) {
        return jobRepository.findByClientId(clientId);
    }
}

