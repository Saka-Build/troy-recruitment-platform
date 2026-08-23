package com.troy.ats.service.impl;

import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.dto.JobCreateRequest;
import com.troy.ats.dto.JobDto;
import com.troy.ats.dto.JobsFiltersDto;
import com.troy.ats.entity.ActivityLog;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.populator.JobPopulator;
import com.troy.ats.populator.ReverseJobPopulator;
import com.troy.ats.repository.JobRepository;
import com.troy.ats.searchfilter.dto.JobExportFilter;
import com.troy.ats.searchfilter.dto.JobFilter;
import com.troy.ats.searchfilter.filter.JobSpecification;
import com.troy.ats.service.ActivityLogService;
import com.troy.ats.service.JobService;
import com.troy.ats.service.SessionService;
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
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.troy.ats.util.CommonUtil.getCode;
import static com.troy.ats.util.CommonUtil.logActivity;

@Service("jobService")
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ReverseJobPopulator reverseJobPopulator;
    private final JobPopulator jobPopulator;
    private final SessionService sessionService;
    private final ActivityLogService activityLogService;

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

        //job ID
        String clientName = job.getClient().getName();
        String endClientName = job.getEndClient().getName();
        String jobId = generateJobId(clientName, endClientName);
        job.setJobId(jobId);

        Job savedJob = jobRepository.save(job);

        ActivityLogRequest activityLogRequest = new ActivityLogRequest();
        activityLogRequest.setEntityType( savedJob.getClass().getSimpleName().toLowerCase(Locale.ROOT));
        activityLogRequest.setEntityId(savedJob.getId());
        List<ActivityLog> logs = logActivity(List.of(activityLogRequest), sessionService,false);
        activityLogService.saveAll(logs);

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

        List<ActivityLog> logs = logActivity(request.getActivityLogs(), sessionService,true);
        activityLogService.saveAll(logs);

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

    /**
     *
     * @param clientName
     * @param endClientName
     * @param number
     * @return
     */
    @Override
    public String generateJobId(String clientName, String endClientName) {

        if(Objects.isNull(clientName) || Objects.isNull(endClientName)){
            return null;
        }
        Long number = jobRepository.getNextJobNumber();

        String jobId = String.format("J%s%s%03d",
                getCode(clientName),
                getCode(endClientName),
                number
        );

        return jobId;
    }

    /**
     *
     * @return
     */
    @Override
    public JobsFiltersDto getJobFilters() {

       long totalOpenJobs = getTotalJobsByStatus(JobStatus.OPEN);
        long totalClosedJobs = getTotalJobsByStatus(JobStatus.CLOSED);
        long totalOnHoldJobs = getTotalJobsByStatus(JobStatus.ON_HOLD);

        JobsFiltersDto jobsFiltersDto = new JobsFiltersDto();
        jobsFiltersDto.setTotalOpenJobs(totalOpenJobs);
        jobsFiltersDto.setTotalClosedJobs(totalClosedJobs);
        jobsFiltersDto.setTotalOnHoldJobs(totalOnHoldJobs);

        return jobsFiltersDto;
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
                    "Job Id",
                    "Title",
                    "Client",
                    "End Client",
                    "Location",
                    "Country",
                    "Work Mode",
                    "Job Type",
                    "clientRateAmount",
                    "clientRateCurrency",
                    "clientRatePeriod",
                    "candidateRateAmount",
                    "candidateRateCurrency",
                    "candidateRatePeriod",
                    "skillsRequired",
                    "Priority",
                    "Status",
                    "Lead",
                    "assignedRecruiters",
                    "Industry",
                    "Description",
                    "Description Source",
                    "Template",
                    "Template Name",
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
                int col = 0;

                row.createCell(col++).setCellValue(safe(job.getJobId()));
                row.createCell(col++).setCellValue(safe(job.getTitle()));
                row.createCell(col++).setCellValue(job.getClient() != null ? safe(job.getClient().getName()) : "");
                row.createCell(col++).setCellValue(job.getEndClient() != null ? safe(job.getEndClient().getName()) : "");
                row.createCell(col++).setCellValue(safe(job.getLocation()));
                row.createCell(col++).setCellValue(job.getCountry() != null ? safe(job.getCountry().getName()) : "");
                row.createCell(col++).setCellValue(job.getWorkMode() != null ? job.getWorkMode().name() : "");
                row.createCell(col++).setCellValue(job.getJobType() != null ? job.getJobType().name() : "");
                row.createCell(col++).setCellValue(job.getClientRateAmount() != null ? job.getClientRateAmount().toString() : "");
                row.createCell(col++).setCellValue(job.getClientRateCurrency() != null ? job.getClientRateCurrency().name() : "");
                row.createCell(col++).setCellValue(job.getClientRatePeriod() != null ? job.getClientRatePeriod().name() : "");
                row.createCell(col++).setCellValue(job.getCandidateRateAmount() != null ? job.getCandidateRateAmount().toString() : "");
                row.createCell(col++).setCellValue(job.getCandidateRateCurrency() != null ? job.getCandidateRateCurrency().name() : "");
                row.createCell(col++).setCellValue(job.getCandidateRatePeriod() != null ? job.getCandidateRatePeriod().name() : "");
                if(Objects.nonNull(job.getSkillsRequired())){
                    StringBuffer skills= new StringBuffer();
                    int count = 1;
                    for(String skill : job.getSkillsRequired()){
                        skills.append(skill);
                        if(count != job.getSkillsRequired().length){
                            skills.append(',');
                        }
                        count ++;
                    }
                    row.createCell(col++).setCellValue(skills.toString());
                }else {
                    row.createCell(col++).setCellValue("");
                }
                row.createCell(col++).setCellValue(safe(job.getPriority()));
                row.createCell(col++).setCellValue(job.getStatus() != null ? job.getStatus().name() : "");
                row.createCell(col++).setCellValue(job.getOwner() != null ? safe(job.getOwner().getFullName()) : "");

                if(Objects.nonNull(job.getAssignedRecruiters())){
                    StringBuffer recruiters= new StringBuffer();
                    int count = 1;
                    for(UUID recruiter : job.getAssignedRecruiters()){
                        recruiters.append(recruiter);
                        if(count != job.getAssignedRecruiters().length){
                            recruiters.append(',');
                        }
                        count ++;
                    }
                    row.createCell(col++).setCellValue(recruiters.toString());
                }else {
                    row.createCell(col++).setCellValue("");
                }
                row.createCell(col++).setCellValue(safe(job.getIndustry()));
                row.createCell(col++).setCellValue(safe(job.getDescription()));
                row.createCell(col++).setCellValue(safe(job.getDescriptionSource()));
                row.createCell(col++).setCellValue(Boolean.TRUE.equals(job.getIsTemplate()));
                row.createCell(col++).setCellValue(safe(job.getTemplateName()));
                row.createCell(col++).setCellValue(job.getCreatedAt() != null ? job.getCreatedAt().toString() : "");
                row.createCell(col++).setCellValue(job.getUpdatedAt() != null ? job.getUpdatedAt().toString() : "");

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