package com.troy.ats.controller;

import com.troy.ats.dto.PipelineDto;
import com.troy.ats.entity.Submission;
import com.troy.ats.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping
    public List<Submission> getAllSubmissions() {
        return submissionService.getAllSubmissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable UUID id) {
        return submissionService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Submission createSubmission(@RequestBody Submission submission) {
        return submissionService.createSubmission(submission);
    }

    @GetMapping("/pipeline")
    public ResponseEntity<List<PipelineDto>> getCandidatePipelines() {
        List<PipelineDto> pipelines = submissionService.getCandidatePipelines();
        return ResponseEntity.ok(pipelines);
    }
}