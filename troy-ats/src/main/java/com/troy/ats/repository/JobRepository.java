package com.troy.ats.repository;

import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID>, JpaSpecificationExecutor<Job> {

    long countByStatus(JobStatus status);
    long countByPriority(String priority);

    List<Job> findByStatusIn(List<JobStatus> statuses);

    @Override
    @EntityGraph(attributePaths = {
            "country",
            "client",
            "endClient",
            "owner"
    })
    Page<Job> findAll(Specification<Job> specification, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {
            "country",
            "client",
            "endClient",
            "owner"
    })
    Optional<Job> findById(UUID id);

    @Query(value = "SELECT nextval('job_number_seq')", nativeQuery = true)
    Long getNextJobNumber();


}