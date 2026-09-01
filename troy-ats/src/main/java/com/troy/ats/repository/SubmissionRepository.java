package com.troy.ats.repository;

import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID>, JpaSpecificationExecutor<Submission> {

    long countByPipelineStage(PipelineStage pipelineStage);
    List<Submission> findByPipelineStageIn(Collection<PipelineStage> pipelineStages);

    @Override
    @EntityGraph(attributePaths = {"status", "subStatus"})
    Optional<Submission> findById(UUID id);

    @EntityGraph(attributePaths = {"status", "subStatus"})
    List<Submission> findByPipelineStage(PipelineStage pipelineStage);

    @Override
    @EntityGraph(attributePaths = {"status", "subStatus"})
    Page<Submission> findAll(Specification<Submission> specification, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"status", "subStatus"})
    List<Submission> findAll(Specification<Submission> specification, Sort sort);

    @Query("""
    SELECT s.job.title
    FROM Submission s
    WHERE s.pipelineStage = :pipelineStage
    """)
    List<String> findJobTitlesByPipelineStage(
            @Param("pipelineStage") PipelineStage pipelineStage
    );

    @Query("""
        SELECT COUNT(s)
        FROM Submission s
        WHERE LOWER(s.status.name) = LOWER(:statusName)
          AND LOWER(s.subStatus.name) = LOWER(:subStatusName)
    """)
    long countSubmissionsByStatusAndSubStatus(
            @Param("statusName") String statusName,
            @Param("subStatusName") String subStatusName
    );

    @Query("""
        SELECT COUNT(s)
        FROM Submission s
        WHERE LOWER(s.status.name) = LOWER(:statusName)
    """)
    long countSubmissionsByStatus(
            @Param("statusName") String statusName
    );

    List<Submission> findByStatus_NameIgnoreCaseAndSubStatus_NameIgnoreCase(
            String statusName,
            String subStatusName
    );

}