package com.troy.ats.controller;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Candidate;
import com.troy.ats.exception.ApiResponse;
import com.troy.ats.searchfilter.dto.CandidateFilter;
import com.troy.ats.service.CandidateService;
import com.troy.ats.service.FileStorageService;
import com.troy.ats.util.CommonUtil;
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
import java.net.URL;
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

    private final FileStorageService fileStorageService;

    public CandidateController(CandidateService candidateService, FileStorageService fileStorageService) {
        this.candidateService = candidateService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CANDIDATE_READ')")
    public ResponseEntity<Page<CandidateDto>> getCandidates(@RequestParam(required = false) String search,
                                                             @RequestParam(required = false) String status,
                                                             @RequestParam(required = false) OffsetDateTime createdFrom,
                                                             @RequestParam(required = false) OffsetDateTime createdTo,
                                                             @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        CandidateFilter filter = new CandidateFilter(search, status, createdFrom, createdTo);

        return ResponseEntity.ok(candidateService.getCandidates(filter, pageable));
    }

    @GetMapping("/candidatefilters")
    @PreAuthorize("hasAuthority('CANDIDATE_READ')")
    public ResponseEntity<CandidatesFiltersDto> getCandidateFilters() {

        return ResponseEntity.ok(candidateService.getCandidateFilters());
    }

    @GetMapping("/allcandidates")
    @PreAuthorize("hasAuthority('CANDIDATE_READ')")
    public List<Candidate> getAllCandidates() {

        return candidateService.getAllCandidates();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CANDIDATE_READ')")
    public ResponseEntity<CandidateDto> getCandidateById(@PathVariable UUID id) {

        return ResponseEntity.ok(candidateService.getCandidateDtoById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CANDIDATE_WRITE')")
    public Candidate createCandidate(@RequestBody Candidate candidate) {
        return candidateService.createCandidate(candidate);
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('CANDIDATE_WRITE')")
    public ResponseEntity<CandidateDto> createCandidateWithCV(
            @RequestPart("candidate") CandidateCreateRequest candidate,
            @RequestPart(value = "original_cv_file", required = false) MultipartFile originalCVFile,
            @RequestPart(value = "troy_cv_file", required = false) MultipartFile troyCVFile) {

        CandidateDto candidateDto = candidateService.createCandidate(candidate, originalCVFile,troyCVFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(candidateDto);
    }

    @PutMapping("update/{candidateId}")
    @PreAuthorize("hasAuthority('CANDIDATE_WRITE')")
    public ResponseEntity<CandidateDto> updateCandidate(
            @PathVariable UUID candidateId,
            @RequestPart("candidate") CandidateCreateRequest candidate,
            @RequestPart(value = "original_cv_file", required = false) MultipartFile originalCVFile,
            @RequestPart(value = "troy_cv_file", required = false) MultipartFile troyCVFile) {


        CandidateDto updatedCandidate = candidateService.updateCandidate(candidateId, candidate, originalCVFile,troyCVFile);

        return ResponseEntity.ok(updatedCandidate);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CANDIDATE_WRITE')")
    public Candidate updateCandidate(
            @PathVariable UUID id,
            @RequestBody Candidate candidate
    ) {
        return candidateService.updateCandidate(id, candidate);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('CANDIDATE_DELETE')")
    public void deleteCandidate(@PathVariable UUID id) {
        candidateService.deleteCandidate(id);
    }

    /**
     * Returns a short-lived S3 URL rather than the bytes, so the download does
     * not stream through the application. The client opens the returned url.
     */
    @GetMapping("/{candidateId}/download/cv/{cvType}")
    @PreAuthorize("hasAuthority('CANDIDATE_READ')")
    public ResponseEntity<Resource> downloadCv(
    public ResponseEntity<FileDownloadDto> downloadCv(
            @PathVariable UUID candidateId, @PathVariable String cvType) {

        Candidate candidate = candidateService.getCandidateById(candidateId);
        if(Objects.isNull(candidate)){
            return ResponseEntity.notFound().build();
        }
        String cvKey = cvType.toLowerCase(Locale.ROOT).contains(CANDIDATE_CV_TYPE_TROY) ? candidate.getTroyCvUrl() : candidate.getOriginalCvUrl();

        if (Objects.isNull(cvKey) || cvKey.isBlank()) {
            return ResponseEntity.notFound().build();
        }

        String fileName = CommonUtil.getFileName(cvKey);
        URL url = fileStorageService.presignedUrl(cvKey, fileName);

        return ResponseEntity.ok(new FileDownloadDto(
                url.toString(), fileName, fileStorageService.getPresignTtlMinutes()));
    }

    @PostMapping("/{id}/send/email/{emailType}")
    @PreAuthorize("hasAuthority('CANDIDATE_READ')")
    public ResponseEntity<ApiResponse> sendCandidateEmail(
            @PathVariable UUID id,
            @PathVariable String emailType,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        candidateService.sendCandidateEmail(id, emailType, file);

        return ResponseEntity.ok(ApiResponse.success("Email sent successfully"));
    }

    @PostMapping("/export")
    @PreAuthorize("hasAuthority('CANDIDATE_READ')")
    public ResponseEntity<byte[]> exportCandidates(
            @RequestBody CandidateExportRequest request)
            throws IOException {

        byte[] file = candidateService.exportCandidates(request);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=candidates.xls")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

}