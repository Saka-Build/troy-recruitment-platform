package com.troy.ats.service;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.searchfilter.dto.SubmissionExportFilter;
import com.troy.ats.searchfilter.dto.SubmissionFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface SubmissionService {

    /**
     *
     * @return
     */
    public List<Submission> getAllSubmissions();

    /**
     *
     * @param id
     * @return
     */
    public Submission getSubmissionById(UUID id);

    /**
     *
     * @param id
     * @return
     */
    SubmissionDto getSubmissionDtoById(UUID id);

    /**
     *
     * @param request
     * @return
     */
    public SubmissionDto createSubmission(SubmissionCreateRequest request);

    /**
     *
     * @param submissionId
     * @param request
     * @return
     */
    SubmissionDto updateSubmission(UUID submissionId, SubmissionCreateRequest request);

    /**
     *
     * @param id
     */
    public void deleteSubmission(UUID id) ;

    /**
     *
     * @param pipelineStage
     * @return
     */
    public long getTotalCVSubmissionsByPipelineStage(PipelineStage pipelineStage);

    /**
     *
     * @return
     */
    public List<PipelineDto> getCandidatePipelines();

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    Page<SubmissionDto> getSubmissions(SubmissionFilter filter, Pageable pageable);

    /**
     *
     * @param pipelineStage
     * @return
     */
    List<String> findJobNamesByPipelineStage(String pipelineStage);

    /**
     *
     * @return
     */
    CountSubmissionsByPipelineStageDto submissionCountsByPipelines();

    /**
     *
     * @param statusName
     * @param subStatusName
     * @return
     */
    long countSubmissionsByStatusAndSubStatus(String statusName, String subStatusName);

    /**
     *
     * @param statusName
     * @return
     */
    long countSubmissionsByStatus(String statusName);

    /**
     *
     * @param statusName
     * @param subStatusName
     * @return
     */
    List<Submission> findByStatus_NameIgnoreCaseAndSubStatus_NameIgnoreCase(String statusName, String subStatusName);

    /**
     *
     * @return
     */
    SubmissionFiltersDto getSubmissionFilters();

    /**
     *
     * @param filter
     * @return
     * @throws IOException
     */
    byte[] exportSubmissions(SubmissionExportFilter filter) throws IOException;

}