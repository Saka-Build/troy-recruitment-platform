package com.troy.ats.controller;

import com.troy.ats.dto.JobCreateRequest;
import com.troy.ats.dto.JobDto;
import com.troy.ats.entity.Job;
import com.troy.ats.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable UUID id) {

        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<JobDto> createJob(@RequestBody JobCreateRequest request) {

        JobDto response = jobService.createJob(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job job) {
       // job.setId(id);
        return jobService.updateJob(id, job);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable UUID id) {
        jobService.deleteJob(id);
    }
}