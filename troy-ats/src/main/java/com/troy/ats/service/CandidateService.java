package com.troy.ats.service;

import com.troy.ats.constants.CommonConstants;
import com.troy.ats.dto.CandidatesDto;
import com.troy.ats.dto.CandidatesFiltersDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Status;
import com.troy.ats.populator.CandidatesPopulator;
import com.troy.ats.repository.CandidateRepository;
import com.troy.ats.repository.JobRepository;
import com.troy.ats.repository.StatusRepository;
import com.troy.ats.repository.SubStatusRepository;
import com.troy.ats.searchfilter.dto.CandidateFilter;
import com.troy.ats.searchfilter.filter.CandidateSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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


    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidateById(UUID id) {
        return candidateRepository.findById(id);
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
        List<Candidate> candidates =  candidateRepository.findAll();
        List<Candidate> filteredCandidates = candidates.stream().filter(candidate -> Objects.nonNull(candidate.getStatus())).toList();
        List<Status> statusList = statusRepository.findAll();

        Map<String, Long> countByCandidateStatus = new HashMap<>();
        countByCandidateStatus.put("Total",(long)candidates.size());

        statusList.forEach(status -> {
            long count = filteredCandidates.stream().filter(candidate ->
                            candidate.getStatus() != null && candidate.getStatus().getId().equals(status.getId())
                    ).count();
            countByCandidateStatus.put(status.getName(), count);
        });
        /*Map<String, Long> countByStatus = filteredCandidates.stream()
                .collect(Collectors.groupingBy(
                        candidate -> candidate.getStatus().getName(),
                        Collectors.counting()
                ));*/

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
}

