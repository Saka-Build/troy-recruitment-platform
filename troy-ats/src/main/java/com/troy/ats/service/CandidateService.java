package com.troy.ats.service;

import com.troy.ats.constants.CommonConstants;
import com.troy.ats.dto.CandidateCreateRequest;
import com.troy.ats.dto.CandidateDto;
import com.troy.ats.dto.CandidatesDto;
import com.troy.ats.dto.CandidatesFiltersDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.enums.CvFormat;
import com.troy.ats.populator.CandidatePopulator;
import com.troy.ats.populator.CandidatesPopulator;
import com.troy.ats.populator.ReverseCandidatePopulator;
import com.troy.ats.repository.CandidateRepository;
import com.troy.ats.repository.JobRepository;
import com.troy.ats.repository.StatusRepository;
import com.troy.ats.repository.SubStatusRepository;
import com.troy.ats.searchfilter.dto.CandidateFilter;
import com.troy.ats.searchfilter.filter.CandidateSpecification;
import com.troy.ats.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final StatusRepository statusRepository;
    private final SubStatusRepository subStatusRepository;
    private final JobRepository jobRepository;
    private final CandidatesPopulator candidatesPopulator;
    private final CandidatePopulator candidatePopulator;
    private final ReverseCandidatePopulator reverseCandidatePopulator;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;


    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidateById(UUID id) {
        return candidateRepository.findById(id);
    }

    public Optional<CandidateDto> getCandidateDtoById(UUID id) {

        Optional<CandidateDto> candidateDto = candidateRepository.findById(id)
                .map(candidate -> {
                    CandidateDto dto = new CandidateDto();
                    candidatePopulator.populate(candidate, dto);
                    return dto;
                });
        return candidateDto;
    }

    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(UUID id, Candidate candidate) {
       // candidate.setId(id);
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(UUID id) {
        candidateRepository.deleteById(id);
    }

    public long getTotalCandidatesByStatus(boolean active) {
        return candidateRepository.countByStatus_Active(active);
    }
    public long getTotalCandidatesByStatusName(String statusName) {
        return candidateRepository.countByStatus_Name(statusName);
    }
    public List<Candidate> getCandidatesByStatusNameANDSubStatusName(String statusName, String subStatusName) {
        return candidateRepository.findByStatus_NameAndSubStatus_Name(statusName, subStatusName);
    }

    public Page<CandidatesDto> getCandidates(CandidateFilter filter, Pageable pageable) {

        return candidateRepository.findAll(CandidateSpecification.filter(filter), pageable)
                .map(candidate -> {
                    CandidatesDto dto = new CandidatesDto();
                    candidatesPopulator.populate(candidate, dto);
                    return dto;
                });
    }

    public CandidatesFiltersDto getCandidateFilters() {

        Map<UUID, Long> counts = candidateRepository.countCandidatesByStatus().stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> (Long) row[1]
                ));

        List<Status> statusList = statusRepository.findAll();
        long totalActiveCandidates = candidateRepository.countByStatus_Active(Boolean.TRUE);
        long countTotalCandidates = candidateRepository.count();

        Map<String, Long> countByCandidateStatus = new HashMap<>();
        countByCandidateStatus.put("Total",countTotalCandidates);
        countByCandidateStatus.put("ActiveCandidates",totalActiveCandidates);
        statusList.forEach(status -> {
            countByCandidateStatus.put(status.getName(), counts.getOrDefault(status.getId(),0L));
        });

        List<Map<UUID, String>> statuses = statusList.stream().map(status -> Map.of(
                        status.getId(),
                        status.getName()
                )).toList();
        List<Map<UUID, String>> subStatuses = subStatusRepository.findAll().stream().map(status -> Map.of(
                status.getId(),
                status.getName()
        )).toList();
        List<Map<UUID, String>> jobs = jobRepository.findAll().stream().map(status -> Map.of(
                status.getId(),
                status.getTitle()
        )).toList();

        Map<String, List<Map<UUID, String>>> filterDropDowns = new HashMap<>();
        filterDropDowns.put(CommonConstants.STATUS_DROPDOWN, statuses);
        filterDropDowns.put(CommonConstants.SUB_STATUS_DROPDOWN, subStatuses);
        filterDropDowns.put(CommonConstants.JOB_DROPDOWN, jobs);

        CandidatesFiltersDto candidatesFiltersDto = new CandidatesFiltersDto();
        candidatesFiltersDto.setCountByStatus(countByCandidateStatus);
        candidatesFiltersDto.setFilterDropDowns(filterDropDowns);

        return candidatesFiltersDto;
    }

    @Transactional
    public CandidateDto createCandidate(CandidateCreateRequest request, MultipartFile originalCVFile, MultipartFile troyCVFile) {

        Candidate candidate = new Candidate();
        reverseCandidatePopulator.populate(request, candidate);

        // Save candidate first
        candidate = candidateRepository.save(candidate);

        // Upload CV
        if (originalCVFile != null && !originalCVFile.isEmpty()) {

            CvFormat originalCVformat = CommonUtil.determineCvFormat(originalCVFile);

            String originalCVFileUrl = fileStorageService.store(originalCVFile, candidate.getId(), Boolean.TRUE);

            candidate.setOriginalCvUrl(originalCVFileUrl);
            candidate.setOriginalCvFormat(originalCVformat);

            candidateRepository.save(candidate);
        }

        if (troyCVFile != null && !troyCVFile.isEmpty()) {

            CvFormat troyCVformat = CommonUtil.determineCvFormat(troyCVFile);

            String troyCVFileUrl = fileStorageService.store(troyCVFile, candidate.getId(), Boolean.FALSE);

            candidate.setOriginalCvUrl(troyCVFileUrl);

            candidateRepository.save(candidate);
        }

        CandidateDto candidateDto = new CandidateDto();
        //candidatePopulator.populate(candidate, candidateDto);

        return candidateDto;
    }

    public void sendCandidateEmail(UUID candidateId, String emailType, MultipartFile file) {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        emailService.sendCandidateEmail(candidate, emailType, file);
    }
}

