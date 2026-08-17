package com.troy.ats.service;

import com.troy.ats.constants.CommonConstants;
import com.troy.ats.dto.*;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Status;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.sql.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

        Optional<CandidateDto> candidateDto = candidateRepository.findCandidateWithDetailsById(id)
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
        return candidateRepository.countByActive(active);
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
        long totalActiveCandidates = candidateRepository.countByActive(Boolean.TRUE);
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
        candidate.setActive(Boolean.TRUE);

        // Save candidate first
        candidate = candidateRepository.save(candidate);

        // Upload CV
        if (originalCVFile != null && !originalCVFile.isEmpty()) {

            CvFormat originalCVformat = CommonUtil.determineCvFormat(originalCVFile);
            String originalCVFileUrl = fileStorageService.store(originalCVFile, candidate.getId(), Boolean.TRUE, Boolean.FALSE);

            candidate.setOriginalCvUrl(originalCVFileUrl);
            candidate.setOriginalCvFormat(originalCVformat);
            candidateRepository.save(candidate);
        }

        if (troyCVFile != null && !troyCVFile.isEmpty()) {

            CvFormat troyCVformat = CommonUtil.determineCvFormat(troyCVFile);
            String troyCVFileUrl = fileStorageService.store(troyCVFile, candidate.getId(), Boolean.FALSE, Boolean.FALSE);

            candidate.setTroyCvUrl(troyCVFileUrl);
            candidateRepository.save(candidate);
        }

        CandidateDto candidateDto = new CandidateDto();
        candidatePopulator.populate(candidate, candidateDto);

        return candidateDto;
    }

    @Transactional
    public CandidateDto updateCandidate(UUID candidateId, CandidateCreateRequest request, MultipartFile originalCVFile, MultipartFile troyCVFile) {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found: " + candidateId));

        reverseCandidatePopulator.populate(request, candidate);
        // Save candidate first
        candidate = candidateRepository.save(candidate);

        if (originalCVFile != null && !originalCVFile.isEmpty()) {

            CvFormat originalCVformat = CommonUtil.determineCvFormat(originalCVFile);
            // Keep old URL before replacing
            String oldCvUrl = candidate.getOriginalCvUrl();
            // Delete old CV
            if (oldCvUrl != null && !oldCvUrl.isBlank()) {

                fileStorageService.delete(oldCvUrl);
            }
            // Store new file
            String originalCVFileUrl = fileStorageService.store(originalCVFile, candidate.getId(), Boolean.TRUE, Boolean.FALSE);

            candidate.setOriginalCvUrl(originalCVFileUrl);
            candidate.setOriginalCvFormat(originalCVformat);
            candidateRepository.save(candidate);
        }

        if (troyCVFile != null && !troyCVFile.isEmpty()) {

            CvFormat troyCVformat = CommonUtil.determineCvFormat(troyCVFile);
            // Keep old URL before replacing
            String oldCvUrl = candidate.getTroyCvUrl();
            // Delete old CV
            if (oldCvUrl != null && !oldCvUrl.isBlank()) {

                fileStorageService.delete(oldCvUrl);
            }
            // Store new file
            String troyCVFileUrl = fileStorageService.store(troyCVFile, candidate.getId(), Boolean.FALSE, Boolean.FALSE);

            candidate.setTroyCvUrl(troyCVFileUrl);
            candidateRepository.save(candidate);
        }
        CandidateDto candidateDto = new CandidateDto();
        candidatePopulator.populate(candidate, candidateDto);

        return candidateDto;

    }

    public void sendCandidateEmail(UUID candidateId, String emailType, MultipartFile file) {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        emailService.sendCandidateEmail(candidate, emailType, file);
    }

    @Transactional(readOnly = true)
    public byte[] exportCandidates(CandidateExportRequest request)
            throws IOException {

        List<Candidate> candidates =
                candidateRepository.findCandidatesForExport(
                        request.getFromDate(),
                        request.getToDate(),
                        request.getLocation(),
                        request.getActive(),
                        request.getStatusId(),
                        request.getSkill()
                );

        try (
                HSSFWorkbook workbook = new HSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {

            Sheet sheet = workbook.createSheet("Candidates");
            createHeader(sheet);
            int rowIndex = 1;

            for (Candidate candidate : candidates) {
                Row row = sheet.createRow(rowIndex++);
                int column = 0;

                setCell(row, column++, candidate.getCvId());
                setCell(row, column++, candidate.getFullName());
                setCell(row, column++, candidate.getEmail());
                setCell(row, column++, candidate.getPhone());
                setCell(row, column++, candidate.getWhatsapp());
                setCell(row, column++, candidate.getLocation());
                setCell(row, column++, candidate.getNationality());
                setCell(row, column++, candidate.getCurrentDesignation());
                setCell(row, column++, candidate.getCurrentEmployer());

                setCell(row, column++, candidate.getExperienceYears() != null ? candidate.getExperienceYears().toString() : "");
                setCell(row, column++, candidate.getNoticePeriodDays() != null ? candidate.getNoticePeriodDays().toString() : "");
                setCell(row, column++, candidate.getCurrentSalary() != null ? candidate.getCurrentSalary().toString() : "");
                setCell(row, column++, candidate.getExpectedSalary() != null ? candidate.getExpectedSalary().toString() : "");
                setCell(row, column++, candidate.getSalaryCurrency());
                setCell(row, column++, candidate.getSkills() != null ? String.join(", ", candidate.getSkills()) : "");

                setCell(row, column++, candidate.getEducation());
                setCell(row, column++, candidate.getVisaStatus());
                setCell(row, column++, candidate.getLinkedinUrl());
                setCell(row, column++, candidate.getSource());

                // Status
                setCell(row, column++, candidate.getStatus() != null ? candidate.getStatus().getName() : "");

                // Sub status
                setCell(row, column++, candidate.getSubStatus() != null ? candidate.getSubStatus().getName() : "");

                // CV Owner
                setCell(row, column++, candidate.getCvOwner() != null ? candidate.getCvOwner().getFullName() : "");

                setCell(row, column++, candidate.getReferredBy());
                setCell(row, column++, candidate.getReferenceNote());
                setCell(row, column++, candidate.getOriginalCvUrl());
                setCell(row, column++, candidate.getOriginalCvFormat() != null ? candidate.getOriginalCvFormat().name() : "");
                setCell(row, column++, candidate.getTroyCvUrl());
                setCell(row, column++, candidate.getTroyCvPdfUrl());
                setCell(row, column++, candidate.getActive() != null ? candidate.getActive().toString() : "");
                setCell(row, column++, candidate.getCreatedAt() != null ? candidate.getCreatedAt().toString() : "");
                setCell(row, column++, candidate.getUpdatedAt() != null ? candidate.getUpdatedAt().toString() : "");
            }

            // Auto-size columns
            for (int i = 0; i < 30; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();
        }
    }

    private void createHeader(Sheet sheet) {

        Row header = sheet.createRow(0);

        String[] columns = {
                "CV ID",
                "Full Name",
                "Email",
                "Phone",
                "WhatsApp",
                "Location",
                "Nationality",
                "Current Designation",
                "Current Employer",
                "Experience Years",
                "Notice Period Days",
                "Current Salary",
                "Expected Salary",
                "Salary Currency",
                "Skills",
                "Education",
                "Visa Status",
                "LinkedIn",
                "Source",
                "Status",
                "Sub Status",
                "CV Owner",
                "Referred By",
                "Reference Note",
                "Original CV URL",
                "Original CV Format",
                "Troy CV URL",
                "Troy CV PDF URL",
                "Active",
                "Created At",
                "Updated At"
        };

        CellStyle style = sheet.getWorkbook().createCellStyle();

        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);

        style.setFont(font);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(style);
        }
    }

    private void setCell(Row row, int column, String value) {

        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
    }
}

