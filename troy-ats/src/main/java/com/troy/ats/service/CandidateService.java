package com.troy.ats.service;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Candidate;
import com.troy.ats.enums.CandidateStatus;
import com.troy.ats.enums.CvFormat;
import com.troy.ats.populator.CandidatePopulator;
import com.troy.ats.populator.ReverseCandidatePopulator;
import com.troy.ats.repository.CandidateRepository;
import com.troy.ats.searchfilter.dto.CandidateFilter;
import com.troy.ats.searchfilter.filter.CandidateSpecification;
import com.troy.ats.util.CommonUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.troy.ats.util.CommonUtil.*;


@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidatePopulator candidatePopulator;
    private final ReverseCandidatePopulator reverseCandidatePopulator;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;


    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(UUID id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found: " + id));
    }

    public CandidateDto getCandidateDtoById(UUID id) {

        CandidateDto candidateDto = candidateRepository.findCandidateWithDetailsById(id)
                .map(candidate -> {
                    CandidateDto dto = new CandidateDto();
                    candidatePopulator.populate(candidate, dto);
                    return dto;
                }).orElseThrow(() -> new EntityNotFoundException("Candidate not found: " + id));
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

    public long getTotalCandidatesByStatusName(String statusName) {
        return candidateRepository.countByStatus(CandidateStatus.fromValue(statusName));
    }

    public Page<CandidateDto> getCandidates(CandidateFilter filter, Pageable pageable) {

        return candidateRepository.findAll(CandidateSpecification.filter(filter), pageable)
                .map(candidate -> {
                    CandidateDto dto = new CandidateDto();
                    candidatePopulator.populate(candidate, dto);
                    return dto;
                });
    }

    public CandidatesFiltersDto getCandidateFilters() {
        CandidatesFiltersDto candidatesFiltersDto = new CandidatesFiltersDto();

        long totalCandidates = candidateRepository.count();
        long totalActiveCandidates = candidateRepository.countByStatus(CandidateStatus.ACTIVE);
        long totalInActiveCandidates = candidateRepository.countByStatus(CandidateStatus.INACTIVE);
        long totalBlackListedCandidates = candidateRepository.countByStatus(CandidateStatus.BLACKLISTED);

        List<String> statuses = List.of(
              enumToStringFormat(CandidateStatus.ACTIVE.name()),
                enumToStringFormat(CandidateStatus.INACTIVE.name()),
                enumToStringFormat(CandidateStatus.BLACKLISTED.name())
        );

        candidatesFiltersDto.setTotalCandidates(totalCandidates);
        candidatesFiltersDto.setTotalActiveCandidates(totalActiveCandidates);
        candidatesFiltersDto.setTotalInActiveCandidates(totalInActiveCandidates);
        candidatesFiltersDto.setTotalBackListedCandidates(totalBlackListedCandidates);
        candidatesFiltersDto.setStatusList(statuses);


        return candidatesFiltersDto;
    }

    @Transactional
    public CandidateDto createCandidate(CandidateCreateRequest request, MultipartFile originalCVFile, MultipartFile troyCVFile) {

        Candidate candidate = new Candidate();
        reverseCandidatePopulator.populate(request, candidate);

        String ownerName = Objects.nonNull(candidate.getCvOwner()) ? candidate.getCvOwner().getFullName() : "";
        String cvId = generateCandidateCVId(ownerName, candidate.getSource());
        candidate.setCvId(cvId);

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

                fileStorageService.delete(oldCvUrl, Boolean.TRUE, Boolean.FALSE);
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

                fileStorageService.delete(oldCvUrl, Boolean.FALSE, Boolean.FALSE);
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

    public String generateCandidateCVId(String ownerName, String sourceName) {

        if(Objects.isNull(ownerName) || Objects.isNull(sourceName)){
            return null;
        }
        Long number = candidateRepository.getNextCandidateNumber();

        String cvId = String.format("J%s%s%03d",
                getCode(ownerName),
                getCodeWithOneLetter(sourceName),
                number
        );

        return cvId;
    }

    public void sendCandidateEmail(UUID candidateId, String emailType, MultipartFile file) {

        Candidate candidate = getCandidateById(candidateId);

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
                        CandidateStatus.fromValue(request.getStatus()),
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
                setCell(row, column++, candidate.getCurrentDesignation());
                // CV Owner
                setCell(row, column++, candidate.getCvOwner() != null ? candidate.getCvOwner().getFullName() : "");
                setCell(row, column++, candidate.getReferredBy());
                setCell(row, column++, candidate.getReferenceNote());
                setCell(row, column++, candidate.getEmail());
                setCell(row, column++, candidate.getPhone());
                setCell(row, column++, candidate.getWhatsapp());
                setCell(row, column++, candidate.getLocation());
                setCell(row, column++, candidate.getNationality());
                setCell(row, column++, candidate.getCurrentEmployer());
                setCell(row, column++, candidate.getExperienceYears() != null ? candidate.getExperienceYears().toString() : "");
                setCell(row, column++, candidate.getSkills() != null ? String.join(", ", candidate.getSkills()) : "");
                setCell(row, column++, candidate.getNoticePeriodDays() != null ? candidate.getNoticePeriodDays().toString() : "");
                setCell(row, column++, candidate.getVisaStatus());
                setCell(row, column++, candidate.getSource());
                setCell(row, column++, candidate.getLinkedinUrl());
                setCell(row, column++, candidate.getStatus() != null ? candidate.getStatus().name(): "");
                setCell(row, column++, candidate.getEducation());
                setCell(row, column++, candidate.getCurrentSalaryAmount() != null ? candidate.getCurrentSalaryAmount().toString(): "");
                setCell(row, column++, candidate.getCurrentSalaryCurrency() != null ? candidate.getCurrentSalaryCurrency().name(): "");
                setCell(row, column++, candidate.getCurrentSalaryPeriod() != null ? candidate.getCurrentSalaryPeriod().name(): "");
                setCell(row, column++, candidate.getExpectedSalaryAmount() != null ? candidate.getExpectedSalaryAmount().toString(): "");
                setCell(row, column++, candidate.getExpectedSalaryCurrency() != null ? candidate.getExpectedSalaryCurrency().name(): "");
                setCell(row, column++, candidate.getExpectedSalaryPeriod() != null ? candidate.getExpectedSalaryPeriod().name(): "");
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
                "Current Designation",
                "CV Owner",
                "Referred By",
                "Reference Note",
                "Email",
                "Phone",
                "WhatsApp",
                "Location",
                "Nationality",
                "Current Employer",
                "Experience Years",
                "Skills",
                "Notice Period Days",
                "Visa Status",
                "Source",
                "LinkedIn",
                "Status",
                "Education",
                "currentSalaryAmount",
                "currentSalaryCurrency",
                "currentSalaryPeriod",
                "expectedSalaryAmount",
                "expectedSalaryCurrency",
                "expectedSalaryPeriod",
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

