package com.troy.ats.controller;

import com.troy.ats.entity.Interview;
import com.troy.ats.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping
    public ResponseEntity<Page<Interview>> getAllInterviews(Pageable pageable) {
        return ResponseEntity.ok(interviewService.getAllInterviews(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getInterviewById(@PathVariable UUID id) {
        return interviewService.getInterviewById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<Page<Interview>> getInterviewsByCandidate(@PathVariable UUID candidateId, Pageable pageable) {
        return ResponseEntity.ok(interviewService.getInterviewsByCandidateId(candidateId)
                .stream()
                .collect(org.springframework.data.domain.Pageable.ofSize(pageable.getPageSize()).withPage(pageable.getPageNumber())));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<Page<Interview>> getInterviewsByDate(@PathVariable LocalDate date, Pageable pageable) {
        return ResponseEntity.ok(interviewService.getInterviewsByDate(date)
                .stream()
                .collect(org.springframework.data.domain.Pageable.ofSize(pageable.getPageSize()).withPage(pageable.getPageNumber())));
    }
}

