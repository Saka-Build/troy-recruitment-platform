package com.troy.ats.controller;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Submission;
import com.troy.ats.exception.ApiResponse;
import com.troy.ats.searchfilter.dto.SubmissionExportFilter;
import com.troy.ats.searchfilter.dto.SubmissionFilter;
import com.troy.ats.service.SubmissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/allSubmissions")
    public List<Submission> getAllSubmissions() {
        return submissionService.getAllSubmissions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable UUID id) {

        return ResponseEntity.ok(submissionService.getSubmissionDtoById(id));

    }

    @GetMapping
    public ResponseEntity<Page<SubmissionDto>> getSubmissions(@RequestParam(required = false) String search,
                                                      @RequestParam(required = false) String pipelineStage,
                                                      @RequestParam(required = false) UUID statusId,
                                                      @RequestParam(required = false) String statusName,
                                                      @RequestParam(required = false) String subStatusName,
                                                      @RequestParam(required = false) UUID candidateId,
                                                      @RequestParam(required = false) UUID jobId,
                                                      @RequestParam(required = false) UUID clientId,
                                                      @RequestParam(required = false) OffsetDateTime createdFrom,
                                                      @RequestParam(required = false) OffsetDateTime createdTo,
                                                      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        SubmissionFilter filter = new SubmissionFilter(search, pipelineStage, statusId, statusName, subStatusName, candidateId, jobId, clientId, createdFrom, createdTo);

        return ResponseEntity.ok(submissionService.getSubmissions(filter, pageable));
    }

    @GetMapping("/header/submissionfilters")
    public ResponseEntity<SubmissionFiltersDto> getSubmissionFilters() {

        return ResponseEntity.ok(submissionService.getSubmissionFilters());
    }

    @GetMapping("/allJobsName")
    public List<String> getJobsNameByPipelineStage( @RequestParam(required = true) String pipelineStage) {

        return submissionService.findJobNamesByPipelineStage(pipelineStage);
    }

    @GetMapping("/submissionCounts")
    public  ResponseEntity<CountSubmissionsByPipelineStageDto> submissionCountsByPipelines() {

        return ResponseEntity.ok(submissionService.submissionCountsByPipelines());
    }

    @PostMapping("/create")
    public ResponseEntity<SubmissionDto> createSubmission(@RequestBody SubmissionCreateRequest request) {

        SubmissionDto response =  submissionService.createSubmission(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{submissionId}")
    public ResponseEntity<SubmissionDto> updateSubmission(
            @PathVariable UUID submissionId,
            @RequestBody SubmissionCreateRequest request) {

        SubmissionDto submissionDto = submissionService.updateSubmission(submissionId, request);

        return ResponseEntity.ok(submissionDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteSubmission(@PathVariable UUID id) {

        submissionService.deleteSubmission(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportSubmissions(@RequestBody(required = false) SubmissionExportFilter filter) throws IOException {

        byte[] excelFile = submissionService.exportSubmissions(filter);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=applications.xls")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(excelFile);
    }

    @GetMapping("/pipeline")
    public ResponseEntity<List<PipelineDto>> getCandidatePipelines() {
        List<PipelineDto> pipelines = submissionService.getCandidatePipelines();
        return ResponseEntity.ok(pipelines);
    }
}