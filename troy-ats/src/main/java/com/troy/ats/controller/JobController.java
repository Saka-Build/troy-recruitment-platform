package com.troy.ats.controller;

import com.troy.ats.entity.Job;
import com.troy.ats.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<Page<Job>> getAllJobs(Pageable pageable) {
        return ResponseEntity.ok(jobService.getAllJobs(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable UUID id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<Job>> getJobsByClientId(@PathVariable UUID clientId, Pageable pageable) {
        Page<Job> jobs = jobService.getJobsByClientId(clientId)
                .stream()
                .collect(org.springframework.data.domain.PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        return ResponseEntity.ok(jobs);
    }
}

