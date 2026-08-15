package com.troy.ats.controller;

import com.troy.ats.dto.CandidatesDto;
import com.troy.ats.dto.CandidatesFiltersDto;
import com.troy.ats.entity.Candidate;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<Candidate> getCandidateById(@PathVariable UUID id) {
        return candidateService.getCandidateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Candidate createCandidate(@RequestBody Candidate candidate) {
        return candidateService.createCandidate(candidate);
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

    @GetMapping("/{candidateId}/originalcv")
    public ResponseEntity<Resource> downloadCv(
            @PathVariable UUID candidateId) {

        Optional<Candidate> candidateOptional = candidateService.getCandidateById(candidateId);
        Candidate candidate = candidateOptional.isPresent() ? candidateOptional.get() : null;
        if(Objects.isNull(candidate)){
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(candidate.getOriginalCvUrl());
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

}