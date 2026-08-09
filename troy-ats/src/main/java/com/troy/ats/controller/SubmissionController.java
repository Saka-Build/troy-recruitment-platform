package com.troy.ats.controller;

import com.troy.ats.entity.Submission;
import com.troy.ats.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping
    public ResponseEntity<Page<Submission>> getAllSubmissions(Pageable pageable) {
        return ResponseEntity.ok(submissionService.getAllSubmissions(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable UUID id) {
        return submissionService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<Page<Submission>> getSubmissionsByCandidate(@PathVariable UUID candidateId, Pageable pageable) {
        return ResponseEntity.ok(submissionService.getSubmissionsByCandidateId(candidateId)
                .stream()
                .collect(org.springframework.data.domain.Pageable.ofSize(pageable.getPageSize()).withPage(pageable.getPageNumber())));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<Page<Submission>> getSubmissionsByJob(@PathVariable UUID jobId, Pageable pageable) {
        return ResponseEntity.ok(submissionService.getSubmissionsByJobId(jobId)
                .stream()
                .collect(org.springframework.data.domain.Pageable.ofSize(pageable.getPageSize()).withPage(pageable.getPageNumber())));
    }
}

