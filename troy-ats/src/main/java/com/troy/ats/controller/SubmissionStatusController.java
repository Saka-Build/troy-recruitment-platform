package com.troy.ats.controller;

import com.troy.ats.dto.SubmissionStatus;
import com.troy.ats.dto.SubmissionStatusRequest;
import com.troy.ats.dto.SubmissionStatusesDto;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.exception.ApiResponse;
import com.troy.ats.service.SubmissionService;
import com.troy.ats.service.impl.SubmissionStatusServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionStatusController {

    private final SubmissionService submissionService;
    private final SubmissionStatusServiceImpl submissionStatusService;

    public SubmissionStatusController(SubmissionService submissionService, SubmissionStatusServiceImpl submissionStatusService) {
        this.submissionService = submissionService;
        this.submissionStatusService = submissionStatusService;
    }

    @GetMapping("/status/allStatuses")
    public List<SubmissionStatus> getAllStatuses() {
        return submissionStatusService.getAllActiveStatus();
    }

    @GetMapping("/statuses")
    public ResponseEntity<SubmissionStatusesDto> getStatuses() {

        return ResponseEntity.ok(submissionStatusService.getSubmissionStatuses());
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<SubmissionStatus> getStatusById(@PathVariable UUID id) {

        return ResponseEntity.ok(submissionStatusService.getStatusDtoById(id));

    }

    @PostMapping("/status/create")
    public ResponseEntity<Status> createStatus(@RequestBody SubmissionStatusRequest request) {

        Status response =  submissionStatusService.createStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/status/update/{statusId}")
    public ResponseEntity<Status> updateStatus(
            @PathVariable UUID statusId,
            @RequestBody SubmissionStatusRequest request) {

        Status status = submissionStatusService.UpdateStatus(statusId, request);

        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/status/delete/{id}")
    public ResponseEntity<ApiResponse> deleteStatus(@PathVariable UUID id) {

        submissionStatusService.deleteStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
    }

    @GetMapping("/substatus/allSubStatuses")
    public List<SubmissionStatus> getAllSubStatuses() {
        return submissionStatusService.getAllActiveSubStatus();
    }

    @GetMapping("/substatus/{id}")
    public ResponseEntity<SubmissionStatus> getSubStatusById(@PathVariable UUID id) {

        return ResponseEntity.ok(submissionStatusService.getSubStatusDtoById(id));

    }

    @GetMapping("/substatus/allSubStatuses/{statusId}")
    public ResponseEntity<List<SubmissionStatus>> getAllSubStatuses(@PathVariable UUID statusId) {

        return ResponseEntity.ok(submissionStatusService.getSubstatusesForStatusId(statusId));
    }

    @PostMapping("/substatus/create")
    public ResponseEntity<SubStatus> createSubStatus(@RequestBody SubmissionStatusRequest request) {

        SubStatus response =  submissionStatusService.createSubStatus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("substatus/update/{subStatusId}")
    public ResponseEntity<SubmissionStatus> updateSubStatus(
            @PathVariable UUID subStatusId,
            @RequestBody SubmissionStatusRequest request) {

        SubmissionStatus status = submissionStatusService.UpdateSubStatus(subStatusId, request);

        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/substatus/delete/{id}")
    public ResponseEntity<ApiResponse> deleteSubStatus(@PathVariable UUID id) {

        submissionStatusService.deleteSubStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
    }

}