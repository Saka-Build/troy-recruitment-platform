package com.troy.ats.controller;

import com.troy.ats.dto.InterviewDto;
import com.troy.ats.dto.InterviewScheduleRequest;
import com.troy.ats.entity.Interview;
import com.troy.ats.exception.ApiResponse;
import com.troy.ats.service.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public List<Interview> getAllInterviews() {
        return interviewService.getAllInterviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewDto> getInterviewById(@PathVariable UUID id) {

        return ResponseEntity.ok(interviewService.getInterviewDtoById(id));

    }

    @GetMapping("/submission/{id}")
    public ResponseEntity<List<InterviewDto>> getInterviewsBySubmission(@PathVariable UUID id) {

        return ResponseEntity.ok(interviewService.getInterviewsBySubmissionId(id));

    }

    @GetMapping("/date/{date}")
    public List<Interview> getInterviewsByDate(@PathVariable LocalDate date) {
        return interviewService.getInterviewsByDate(date);
    }

    @PostMapping("/create")
    public ResponseEntity<InterviewDto> createInterview(@RequestBody InterviewScheduleRequest interview) {
        InterviewDto response =  interviewService.createInterview(interview);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/update/{interviewId}")
    public ResponseEntity<InterviewDto> updateInterview(@PathVariable UUID interviewId, @RequestBody InterviewScheduleRequest interview) {
        InterviewDto response =  interviewService.updateInterview(interviewId, interview);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteClient(@PathVariable UUID id) {

        interviewService.deleteInterview(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
    }
}