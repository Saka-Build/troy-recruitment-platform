package com.troy.ats.repository;

import com.troy.ats.entity.Candidate;
import com.troy.ats.enums.CandidateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID>, JpaSpecificationExecutor<Candidate> {

    @Query("""
    SELECT c.status, COUNT(c)
    FROM Candidate c
    GROUP BY c.status
    ORDER BY c.status
    """)
    List<Object[]> countCandidatesByStatus();

    long countByStatus(CandidateStatus status);

    @Query(value = """
    SELECT DISTINCT c.*
    FROM candidates c
    WHERE
        (
            CAST(:fromDate AS timestamptz) IS NULL
            OR c.created_at >= CAST(:fromDate AS timestamptz)
        )

        AND (
            CAST(:toDate AS timestamptz) IS NULL
            OR c.created_at <= CAST(:toDate AS timestamptz)
        )

        AND (
            CAST(:location AS text) IS NULL
            OR LOWER(c.location) LIKE LOWER(
                CONCAT('%', CAST(:location AS text), '%')
            )
        )

        AND (
            CAST(:status AS text) IS NULL
            OR c.status = CAST(:status AS text)
        )

        AND (
            CAST(:skills AS text) IS NULL
            OR EXISTS (
                SELECT 1
                FROM unnest(c.skills) AS candidate_skill
                WHERE LOWER(TRIM(candidate_skill)) IN (
                    SELECT LOWER(TRIM(requested_skill))
                    FROM unnest(
                        string_to_array(
                            CAST(:skills AS text),
                            ','
                        )
                    ) AS requested_skill
                )
            )
        )

    ORDER BY c.created_at DESC
    """, nativeQuery = true)
    List<Candidate> findCandidatesForExport(
            @Param("fromDate") OffsetDateTime fromDate,
            @Param("toDate") OffsetDateTime toDate,
            @Param("location") String location,
            @Param("status") CandidateStatus status,
            @Param("skills") String skills
    );

    @EntityGraph(attributePaths = {
            "cvOwner",
            "createdBy",
            "updatedBy"
    })
    Optional<Candidate> findCandidateWithDetailsById(UUID id);

    @Query(value = "SELECT nextval('candidate_number_seq')", nativeQuery = true)
    Long getNextCandidateNumber();

    @Override
    @EntityGraph(attributePaths = {"cvOwner"})
    Page<Candidate> findAll(Specification<Candidate> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"cvOwner"})
    Optional<Candidate> findById(UUID id);
}