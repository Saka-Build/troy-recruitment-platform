package com.troy.ats.service.impl;

import com.troy.ats.dto.*;
import com.troy.ats.entity.*;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.populator.CandidatePipelinePopulator;
import com.troy.ats.populator.NotePopulator;
import com.troy.ats.populator.ReverseSubmissionPopulator;
import com.troy.ats.populator.SubmissionPopulator;
import com.troy.ats.repository.SubmissionRepository;
import com.troy.ats.searchfilter.dto.SubmissionExportFilter;
import com.troy.ats.searchfilter.dto.SubmissionFilter;
import com.troy.ats.searchfilter.filter.SubmissionSpecification;
import com.troy.ats.service.SubmissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.troy.ats.constants.CommonConstants.ACTIVITY_LOG_ACTION_STATUS_UPDATE;
import static com.troy.ats.constants.CommonConstants.STATUS_APPLIED;
import static com.troy.ats.util.CommonUtil.*;

@Slf4j
@Service("submissionService")
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final CandidatePipelinePopulator candidatePipelinePopulator;
    private final ReverseSubmissionPopulator reverseSubmissionPopulator;
    private final SubmissionStatusServiceImpl submissionStatusService;
    private final SessionServiceImpl sessionService;
    private final SubmissionPopulator submissionPopulator;
    private final ActivityLogServiceImpl activityLogService;
    private final ClientServiceImpl clientService;
    private final JobServiceImpl jobService;
    private final NoteServiceImpl noteService;
    private final NotePopulator notePopulator;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.ENGLISH);


    @Override
    @Transactional(readOnly = true)
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Submission getSubmissionById(UUID id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found: " + id));
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public SubmissionDto getSubmissionDtoById(UUID id) {

        Submission submission = getSubmissionById(id);
        SubmissionDto dto = new SubmissionDto();
        submissionPopulator.populate(submission, dto);
        return dto;
    }

    @Override
    @Transactional
    public SubmissionDto createSubmission(SubmissionCreateRequest request) {
        Submission submission = new Submission();
        reverseSubmissionPopulator.populate(request, submission);
        if(Objects.isNull(submission.getStatus())){

            submission.setPipelineStage(PipelineStage.APPLIED);
            Status status = submissionStatusService.getStatusByName(STATUS_APPLIED);
            submission.setStatus(status);
        }

        submission.setSubmittedBy(sessionService.getCurrentUser());
        submission.setSubmittedAt(Instant.now());

        submission = submissionRepository.save(submission);

        ActivityLogRequest activityLogRequest = new ActivityLogRequest();
        activityLogRequest.setEntityType( submission.getClass().getSimpleName().toLowerCase(Locale.ROOT));
        activityLogRequest.setEntityId(submission.getId());
        List<ActivityLog> logs = logActivity(List.of(activityLogRequest), sessionService,false);
        activityLogService.saveAll(logs);

        SubmissionDto submissionDto = new SubmissionDto();
        submissionPopulator.populate(submission, submissionDto);
        return submissionDto;

    }

    /**
     *
     * @param submissionId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public SubmissionDto updateSubmission(UUID submissionId, SubmissionCreateRequest request) {

        Submission submission = getSubmissionById(submissionId);
        reverseSubmissionPopulator.populate(request, submission);

        // Save submission first
        submission = submissionRepository.save(submission);

        List<ActivityLog> logs = logActivity(request.getActivityLogs(), sessionService,true);
        activityLogService.saveAll(logs);

        SubmissionDto submissionDto = new SubmissionDto();
        submissionPopulator.populate(submission, submissionDto);

        return submissionDto;
    }

    @Override
    public void deleteSubmission(UUID id) {
        submissionRepository.deleteById(id);
    }

    @Override
    public long getTotalCVSubmissionsByPipelineStage(PipelineStage pipelineStage) {
        return submissionRepository.countByPipelineStage(pipelineStage);
    }

    @Override
    public List<PipelineDto> getCandidatePipelines() {

        List<PipelineStage> stages = List.of(
                PipelineStage.APPLIED,
                PipelineStage.SCREENING,
                PipelineStage.READY_TO_SUBMIT,
                PipelineStage.SUBMITTED,
                PipelineStage.INTERVIEW,
                PipelineStage.SELECTED,
                PipelineStage.REJECTED,
                PipelineStage.ONBOARDING,
                PipelineStage.ONBOARDED
        );

        List<Submission> submissions = submissionRepository.findByPipelineStageIn(stages);

        Map<PipelineStage, List<Submission>> groupedSubmissions = submissions.stream().collect(Collectors.groupingBy(Submission::getPipelineStage));

        List<PipelineDto> pipelineDtoList = stages.stream().map(stage -> {
            PipelineDto pipelineDto = new PipelineDto();
            List<Submission> submissionList = groupedSubmissions.getOrDefault(stage, List.of());
            pipelineDto.setPipelineStage(stage);
            pipelineDto.setTotalCandidates(submissionList.size());
            List<CandidatePipelineDto> CandidatePipelineDtoList = submissionList.stream()
                    .map(submission -> {
                        CandidatePipelineDto candidate = new CandidatePipelineDto();
                        candidatePipelinePopulator.populate(submission, candidate);
                        return candidate;
                    }).toList();
            pipelineDto.setCandidates(CandidatePipelineDtoList);
            return pipelineDto;

        }).toList();

        return pipelineDtoList;

    }

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubmissionDto> getSubmissions(SubmissionFilter filter, Pageable pageable) {

        return submissionRepository.findAll(SubmissionSpecification.filter(filter), pageable)
                .map(submission -> {
                    SubmissionDto dto = new SubmissionDto();
                    submissionPopulator.populate(submission, dto);
                    return dto;
                });
    }

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SubmissionDto> getSubmissionsForReport(SubmissionFilter filter, Pageable pageable) {

        Page<Submission> submissionPage = submissionRepository.findAll(SubmissionSpecification.filter(filter), pageable);

        if (submissionPage.isEmpty()) {
            return Page.empty(pageable);
        }

        // Candidate IDs
        List<UUID> candidateIds = submissionPage.stream().map(submission -> submission.getCandidate().getId()).distinct().toList();
        // Submission IDs
        List<UUID> submissionIds = submissionPage.stream().map(Submission::getId).toList();

        // Latest candidate notes
        String candidateType = Candidate.class.getSimpleName().toLowerCase(Locale.ROOT);
        List<Note> candidateNotes = candidateIds.isEmpty() ? Collections.emptyList() : noteService.findLatestNotes(candidateType, candidateIds);
        Map<UUID, Note> latestCandidateNoteMap = candidateNotes.stream().collect(Collectors.toMap(Note::getEntityId, Function.identity()));

        // Latest "Updated status" activity
        String submissionType = Submission.class.getSimpleName().toLowerCase(Locale.ROOT);
        List<ActivityLog> statusActivities = submissionIds.isEmpty() ? Collections.emptyList() : activityLogService.findLatestByEntityTypeAndAction(submissionType, ACTIVITY_LOG_ACTION_STATUS_UPDATE, submissionIds);
        Map<UUID, ActivityLog> latestStatusActivityMap = statusActivities.stream().collect(Collectors.toMap(ActivityLog::getEntityId, Function.identity()));

        return submissionPage.map(submission -> {
                                SubmissionDto dto = new SubmissionDto();
                                submissionPopulator.populate(submission, dto);
                                // Latest note content only
                                UUID candidateId = submission.getCandidate().getId();
                                Note latestCandidateNote = latestCandidateNoteMap.get(candidateId);
                                if(Objects.nonNull(latestCandidateNote)){
                                    NoteDto noteDto = new NoteDto();
                                    notePopulator.populate(latestCandidateNote, noteDto, sessionService);
                                    dto.setLatestCandidateNote(noteDto);
                                }

                                // Latest status update
                                UUID submissionId = submission.getId();
                                ActivityLog latestStatusActivity = latestStatusActivityMap.get(submissionId);
                                if(Objects.nonNull(latestStatusActivity)){
                                    dto.setStatusUpdatedBy(latestStatusActivity.getPerformedBy().getFullName());
                                    LocalDateTime statusUpdatedAt= convertInstantToLocalDate(latestStatusActivity.getPerformedAt(), sessionService);
                                    dto.setStatusUpdatedAt(statusUpdatedAt.format(formatter));
                                }

                                return dto;
                            });
    }

    /**
     *
     * @param pipelineStage
     * @return
     */
    @Override
    public List<String> findJobNamesByPipelineStage(String pipelineStage) {

        PipelineStage pipeline = PipelineStage.fromValue(pipelineStage);
        List<String> jobNames = submissionRepository.findJobTitlesByPipelineStage(pipeline);
        return jobNames;
    }

    /**
     *
     * @return
     */
    @Override
    public CountSubmissionsByPipelineStageDto submissionCountsByPipelines() {

        CountSubmissionsByPipelineStageDto dto = new CountSubmissionsByPipelineStageDto();
        dto.setTotalApplied(getTotalCVSubmissionsByPipelineStage(PipelineStage.APPLIED));
        dto.setTotalScreening(getTotalCVSubmissionsByPipelineStage(PipelineStage.SCREENING));
        dto.setTotalReadyToSubmit(getTotalCVSubmissionsByPipelineStage(PipelineStage.READY_TO_SUBMIT));
        dto.setTotalSubmitted(getTotalCVSubmissionsByPipelineStage(PipelineStage.SUBMITTED));
        dto.setTotalInterview(getTotalCVSubmissionsByPipelineStage(PipelineStage.INTERVIEW));
        dto.setTotalSelected(getTotalCVSubmissionsByPipelineStage(PipelineStage.SELECTED));
        dto.setTotalRejected(getTotalCVSubmissionsByPipelineStage(PipelineStage.REJECTED));
        dto.setTotalOnBoarding(getTotalCVSubmissionsByPipelineStage(PipelineStage.ONBOARDING));
        dto.setTotalOnBoarded(getTotalCVSubmissionsByPipelineStage(PipelineStage.ONBOARDED));

        return dto;

    }

    /**
     *
     * @param statusName
     * @param subStatusName
     * @return
     */
    @Override
    public long countSubmissionsByStatusAndSubStatus(String statusName, String subStatusName) {
        return submissionRepository.countSubmissionsByStatusAndSubStatus(statusName, statusName);
    }

    /**
     *
     * @param statusName
     * @return
     */
    @Override
    public long countSubmissionsByStatus(String statusName) {
        return submissionRepository.countSubmissionsByStatus(statusName);
    }

    /**
     *
     * @param statusName
     * @param subStatusName
     * @return
     */
    @Override
    public List<Submission> findByStatus_NameIgnoreCaseAndSubStatus_NameIgnoreCase(String statusName, String subStatusName) {
        return submissionRepository.findByStatus_NameIgnoreCaseAndSubStatus_NameIgnoreCase(statusName,subStatusName);
    }

    /**
     *
     * @return
     */
    @Override
    public SubmissionFiltersDto getSubmissionFilters() {

        List<ClientsForSubmissionFiltersDto> clients = clientService.findByIsActive(Boolean.TRUE).stream().map(client -> {
                                                ClientsForSubmissionFiltersDto clientDto = new ClientsForSubmissionFiltersDto();
                                                clientDto.setId(client.getId());
                                                clientDto.setName(client.getName());
                                                return clientDto;
                                            }).toList();

        List<JobsForSubmissionFiltersDto> jobs = jobService.findByStatusIn(List.of(JobStatus.OPEN)).stream().map(job -> {
                                            JobsForSubmissionFiltersDto jobsDto = new JobsForSubmissionFiltersDto();
                                            jobsDto.setId(job.getId());
                                            jobsDto.setName(job.getTitle());
                                            return jobsDto;
                                        }).toList();

        List<SubmissionStatus> submissionStatuses = submissionStatusService.getAllActiveStatus();

        SubmissionFiltersDto submissionFiltersDto = new SubmissionFiltersDto();

        submissionFiltersDto.setTotalSubmittedApplications(getTotalCVSubmissionsByPipelineStage(PipelineStage.SUBMITTED));
        submissionFiltersDto.setTotalInterviewApplications(getTotalCVSubmissionsByPipelineStage(PipelineStage.INTERVIEW));
        submissionFiltersDto.setTotalOnboardedApplications(getTotalCVSubmissionsByPipelineStage(PipelineStage.ONBOARDED));
        submissionFiltersDto.setJobs(jobs);
        submissionFiltersDto.setClients(clients);
        submissionFiltersDto.setApplicationStatusList(submissionStatuses);

        return submissionFiltersDto;
    }

    /**
     *
     * @param filter
     * @return
     * @throws IOException
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] exportSubmissions(SubmissionExportFilter filter) throws IOException {

        Specification<Submission> specification = SubmissionSpecification.exportFilter(filter);

        List<Submission> submissions = submissionRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "createdAt"));

        return createExcel(submissions);
    }

    private byte[] createExcel(List<Submission> submissions) throws IOException {

        try (Workbook workbook = new HSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Submissions");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            headerStyle.setFont(headerFont);

            // Header
            Row header = sheet.createRow(0);

            String[] columns = {
                    "Client",
                    "End Client",
                    "Job Name",
                    "BDM",
                    "Priority",
                    "CV Id",
                    "Candidate",
                    "Status",
                    "Sub Status",
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

            for (Submission submission : submissions) {

                Row row = sheet.createRow(rowNum++);
                int col = 0;

                row.createCell(col++).setCellValue(submission.getJob() != null ? submission.getJob().getClient() !=null ?safe(submission.getJob().getClient().getName()) : "" : "");
                row.createCell(col++).setCellValue(submission.getJob() != null ? submission.getJob().getEndClient() !=null ?safe(submission.getJob().getEndClient().getName()) : "" : "");
                row.createCell(col++).setCellValue(submission.getJob()!= null ? safe(submission.getJob().getTitle()) : "");
                row.createCell(col++).setCellValue(submission.getJob() != null ? submission.getJob().getClient() !=null ?safe(submission.getJob().getClient().getSource()) : "" : "");
                row.createCell(col++).setCellValue(submission.getJob()!= null ? safe(enumToStringFormat(submission.getJob().getPriority())) : "");
                row.createCell(col++).setCellValue(submission.getCandidate()!= null ? safe(submission.getCandidate().getCvId()) : "");
                row.createCell(col++).setCellValue(submission.getCandidate()!= null ? safe(submission.getCandidate().getFullName()) : "");
                row.createCell(col++).setCellValue(submission.getStatus()!= null ? safe(submission.getStatus().getName()) : "");
                row.createCell(col++).setCellValue(submission.getSubStatus()!= null ? safe(submission.getSubStatus().getName()) : "");
                row.createCell(col++).setCellValue(submission.getCreatedAt() != null ? submission.getCreatedAt().toString() : "");
                row.createCell(col++).setCellValue(submission.getUpdatedAt() != null ? submission.getUpdatedAt().toString() : "");

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

}