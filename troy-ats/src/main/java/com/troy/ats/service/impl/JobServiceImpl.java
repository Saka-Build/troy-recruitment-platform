package com.troy.ats.service.impl;

import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.JobCreateRequest;
import com.troy.ats.dto.JobDto;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.populator.JobPopulator;
import com.troy.ats.populator.ReverseJobPopulator;
import com.troy.ats.repository.JobRepository;
import com.troy.ats.searchfilter.dto.JobExportFilter;
import com.troy.ats.searchfilter.dto.JobFilter;
import com.troy.ats.searchfilter.filter.EmployeeSpecification;
import com.troy.ats.searchfilter.filter.JobSpecification;
import com.troy.ats.service.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service("jobService")
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ReverseJobPopulator reverseJobPopulator;
    private final JobPopulator jobPopulator;

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found: " + id));
    }

    @Override
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, Job job) {
       // job.setId(id);
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(UUID id) {
        jobRepository.deleteById(id);
    }

    @Override
    public long getTotalJobsByStatus(JobStatus status) {
        return jobRepository.countByStatus(status);
    }

    @Override
    public long getTotalJobsByPriority(String priority) {
        return jobRepository.countByPriority(priority);
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public JobDto createJob(JobCreateRequest request) {

        validateJobRequest(request);
        Job job = new Job();
        reverseJobPopulator.populate(request,job);
        job.setStatus(JobStatus.OPEN);
        Job savedJob = jobRepository.save(job);

        // 8. Convert to DTO
        JobDto jobDto = new JobDto();
        jobPopulator.populate(savedJob, jobDto);

        return jobDto;
    }

    @Override
    @Transactional
    public JobDto updateJob(UUID jobId, JobCreateRequest request) {

        Job job = getJobById(jobId);

        reverseJobPopulator.populate(request, job);
        // Save employee first
        job = jobRepository.save(job);

        JobDto jobDto = new JobDto();
        jobPopulator.populate(job, jobDto);

        return jobDto;
    }

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Override
    public Page<JobDto> getJobs(JobFilter filter, Pageable pageable) {
        return jobRepository.findAll(JobSpecification.filter(filter), pageable)
                .map(job -> {
                    JobDto dto = new JobDto();
                    jobPopulator.populate(job, dto);
                    return dto;
                });
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public JobDto getJobDtoById(UUID id) {

        JobDto jobDto = jobRepository.findById(id)
                .map(job -> {
                    JobDto dto = new JobDto();
                    jobPopulator.populate(job, dto);
                    return dto;
                }) .orElseThrow(() -> new EntityNotFoundException("Job not found: " + id));
        return jobDto;
    }

    /**
     *
     * @param filter
     * @return
     * @throws IOException
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] exportJobs(JobExportFilter filter) throws IOException {

        Specification<Job> specification = JobSpecification.exportFilter(filter);

        List<Job> jobs = jobRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "createdAt"));

        return createExcel(jobs);
    }

    private byte[] createExcel(List<Job> jobs) throws IOException {

        try (Workbook workbook = new HSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Jobs");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            headerStyle.setFont(headerFont);

            // Header
            Row header = sheet.createRow(0);

            String[] columns = {
                    "Title",
                    "Client",
                    "Location",
                    "Country",
                    "Work Mode",
                    "Job Type",
                    "Industry",
                    "Experience Min",
                    "Experience Max",
                    "Salary Min",
                    "Salary Max",
                    "Currency",
                    "Status",
                    "Priority",
                    "Description",
                    "Description Source",
                    "Template",
                    "Template Name",
                    "Openings",
                    "Filled",
                    "Created At",
                    "Updated At"
            };

            for (int i = 0; i < columns.length; i++) {

                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;

            for (Job job : jobs) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(safe(job.getTitle()));
                row.createCell(1).setCellValue(job.getClient() != null ? safe(job.getClient().getName()) : "");
                row.createCell(2).setCellValue(safe(job.getLocation()));
                row.createCell(3).setCellValue(job.getCountry() != null ? safe(job.getCountry().getName()) : "");
                row.createCell(4).setCellValue(job.getWorkMode() != null ? job.getWorkMode().name() : "");
                row.createCell(5).setCellValue(job.getJobType() != null ? job.getJobType().name() : "");
                row.createCell(6).setCellValue(safe(job.getIndustry()));
                row.createCell(7).setCellValue(job.getExperienceMin() != null ? job.getExperienceMin().doubleValue() : 0);
                row.createCell(8).setCellValue(job.getExperienceMax() != null ? job.getExperienceMax().doubleValue() : 0);
                row.createCell(9).setCellValue(job.getSalaryMin() != null ? job.getSalaryMin().doubleValue() : 0);
                row.createCell(10).setCellValue(job.getSalaryMax() != null ? job.getSalaryMax().doubleValue() : 0);
                row.createCell(11).setCellValue(safe(job.getSalaryCurrency()));
                row.createCell(12).setCellValue(job.getStatus() != null ? job.getStatus().name() : "");
                row.createCell(13).setCellValue(safe(job.getPriority()));
                row.createCell(14).setCellValue(safe(job.getDescription()));
                row.createCell(15).setCellValue(safe(job.getDescriptionSource()));
                row.createCell(16).setCellValue(Boolean.TRUE.equals(job.getIsTemplate()));
                row.createCell(17).setCellValue(safe(job.getTemplateName()));
                row.createCell(18).setCellValue(job.getOpeningsCount() != null ? job.getOpeningsCount() : 0);
                row.createCell(19).setCellValue(job.getFilledCount() != null ? job.getFilledCount() : 0);
                row.createCell(20).setCellValue(job.getCreatedAt() != null ? job.getCreatedAt().toString() : "");
                row.createCell(21).setCellValue(job.getUpdatedAt() != null ? job.getUpdatedAt().toString() : "");
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();
        }
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    private String value(String value) {
        return value != null ? value : "";
    }

    private void validateJobRequest(JobCreateRequest request){
        // 4. Validate experience
        if (request.getExperienceMin() != null && request.getExperienceMax() != null
                && request.getExperienceMin().compareTo(request.getExperienceMax()) > 0) {

            throw new IllegalArgumentException("Minimum experience cannot be greater than maximum experience");
        }

        // 5. Validate salary
        if (request.getSalaryMin() != null && request.getSalaryMax() != null
                && request.getSalaryMin().compareTo(request.getSalaryMax()) > 0) {

            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
        }
    }

}