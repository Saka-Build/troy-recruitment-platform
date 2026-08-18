package com.troy.ats.controller;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Candidate;
import com.troy.ats.exception.ApiResponse;
import com.troy.ats.searchfilter.dto.CandidateFilter;
import com.troy.ats.service.CandidateService;
import com.troy.ats.util.CommonUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.troy.ats.constants.CommonConstants.CANDIDATE_CV_TYPE_TROY;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public ResponseEntity<Page<CandidatesDto>> getCandidates(@RequestParam(required = false) String search,
                                                             @RequestParam(required = false) UUID statusId,
                                                             @RequestParam(required = false) UUID subStatusId,
                                                             @RequestParam(required = false) UUID jobId,
                                                             @RequestParam(required = false) Boolean active,
                                                             @RequestParam(required = false) String location,
                                                             @RequestParam(required = false) String source,
                                                             @RequestParam(required = false) OffsetDateTime createdFrom,
                                                             @RequestParam(required = false) OffsetDateTime createdTo,
                                                             @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        CandidateFilter filter = new CandidateFilter(search, statusId, subStatusId, jobId, active, location, source, createdFrom, createdTo);

        return ResponseEntity.ok(candidateService.getCandidates(filter, pageable));
    }

    @GetMapping("/candidatefilters")
    public ResponseEntity<CandidatesFiltersDto> getCandidateFilters() {

        return ResponseEntity.ok(candidateService.getCandidateFilters());
    }

    @GetMapping("/allcandidates")
    @PreAuthorize("isAuthenticated()")
    public List<Candidate> getAllCandidates() {

        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDto> getCandidateById(@PathVariable UUID id) {

        return ResponseEntity.ok(candidateService.getCandidateDtoById(id));
    }

    @PostMapping
    public Candidate createCandidate(@RequestBody Candidate candidate) {
        return candidateService.createCandidate(candidate);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CandidateDto> createCandidateWithCV(
            @RequestPart("candidate") CandidateCreateRequest candidate,
            @RequestPart(value = "original_cv_file", required = false) MultipartFile originalCVFile,
            @RequestPart(value = "troy_cv_file", required = false) MultipartFile troyCVFile) {

        CandidateDto candidateDto = candidateService.createCandidate(candidate, originalCVFile,troyCVFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(candidateDto);
    }

    @PutMapping("update/{candidateId}")
    public ResponseEntity<CandidateDto> updateCandidate(
            @PathVariable UUID candidateId,
            @RequestPart("candidate") CandidateCreateRequest candidate,
            @RequestPart(value = "original_cv_file", required = false) MultipartFile originalCVFile,
            @RequestPart(value = "troy_cv_file", required = false) MultipartFile troyCVFile) {


        CandidateDto updatedCandidate = candidateService.updateCandidate(candidateId, candidate, originalCVFile,troyCVFile);

        return ResponseEntity.ok(updatedCandidate);
    }

    @PutMapping("/{id}")
    public Candidate updateCandidate(
            @PathVariable UUID id,
            @RequestBody Candidate candidate
    ) {
        return candidateService.updateCandidate(id, candidate);
    }

    @DeleteMapping("/{id}")
    public void deleteCandidate(@PathVariable UUID id) {
        candidateService.deleteCandidate(id);
    }

    @GetMapping("/{candidateId}/download/cv/{cvType}")
    public ResponseEntity<Resource> downloadCv(
            @PathVariable UUID candidateId, @PathVariable String cvType) {

        Candidate candidate = candidateService.getCandidateById(candidateId);
        if(Objects.isNull(candidate)){
            return ResponseEntity.notFound().build();
        }
        String cvUrl = cvType.toLowerCase(Locale.ROOT).contains(CANDIDATE_CV_TYPE_TROY) ? candidate.getTroyCvUrl() : candidate.getOriginalCvUrl();

        Path path = Paths.get(cvUrl);
        if (!Files.exists(path) || !Files.isReadable(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(path);
        String fileName = path.getFileName().toString();
        MediaType mediaType = CommonUtil.getMediaType(candidate.getOriginalCvFormat());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\""
                )
                .body(resource);
    }

    @PostMapping("/{id}/send/email/{emailType}")
    public ResponseEntity<ApiResponse> sendCandidateEmail(
            @PathVariable UUID id,
            @PathVariable String emailType,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        candidateService.sendCandidateEmail(id, emailType, file);

        return ResponseEntity.ok(ApiResponse.success("Email sent successfully"));
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportCandidates(
            @RequestBody CandidateExportRequest request)
            throws IOException {

        byte[] file = candidateService.exportCandidates(request);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=candidates.xls")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

}