package com.troy.ats.controller;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.exception.ApiResponse;
import com.troy.ats.searchfilter.dto.JobExportFilter;
import com.troy.ats.searchfilter.dto.JobFilter;
import com.troy.ats.service.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/alljobs")

    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('JOB_READ')")
    public ResponseEntity<Page<JobDto>> getJobs(@RequestParam(required = false) String search,
                                                @RequestParam(required = false) String countryCode,
                                                @RequestParam(required = false) String status,
                                                @RequestParam(required = false) String priority,
                                                @RequestParam(required = false) OffsetDateTime createdFrom,
                                                @RequestParam(required = false) OffsetDateTime createdTo,
                                                @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        JobFilter filter = new JobFilter(search, countryCode,status, priority, createdFrom, createdTo);

        return ResponseEntity.ok(jobService.getJobs(filter, pageable));
    }

    @GetMapping("/jobheader/jobfilters")
    @PreAuthorize("hasAuthority('JOB_READ')")
    public ResponseEntity<JobsFiltersDto> getJobFilters() {

        return ResponseEntity.ok(jobService.getJobFilters());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('JOB_READ')")
    public ResponseEntity<JobDto> getJobById(@PathVariable UUID id) {

        return ResponseEntity.ok(jobService.getJobDtoById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('JOB_WRITE')")
    public ResponseEntity<JobDto> createJob(@RequestBody JobCreateRequest request) {

        JobDto response = jobService.createJob(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/update/{jobId}")
    @PreAuthorize("hasAuthority('JOB_WRITE')")
    public ResponseEntity<JobDto> updateJob(
            @PathVariable UUID jobId,
            @RequestBody JobCreateRequest request) {

        JobDto jobDto = jobService.updateJob(jobId, request);

        return ResponseEntity.ok(jobDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('JOB_WRITE')")
    public Job updateJob(@PathVariable Long id, @RequestBody Job job) {
       // job.setId(id);
        return jobService.updateJob(id, job);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('JOB_DELETE')")
    public ResponseEntity<ApiResponse> deleteJob(@PathVariable UUID id) {

        jobService.deleteJob(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
    }

    @PostMapping("/export")
    @PreAuthorize("hasAuthority('JOB_READ')")
    public ResponseEntity<byte[]> exportJobs(@RequestBody(required = false) JobExportFilter filter) throws IOException {

        byte[] excelFile = jobService.exportJobs(filter);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=jobs.xls")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(excelFile);
    }
}