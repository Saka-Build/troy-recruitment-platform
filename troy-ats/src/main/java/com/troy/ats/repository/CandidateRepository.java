package com.troy.ats.repository;

import com.troy.ats.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID>, JpaSpecificationExecutor<Candidate> {

    @Query("""
    SELECT c.status.id, COUNT(c)
    FROM Candidate c
    WHERE c.status IS NOT NULL
    GROUP BY c.status.id
    """)
    List<Object[]> countCandidatesByStatus();
    long countByActive(boolean active);
    long countByStatus_Name(String statusName);
    List<Candidate> findByStatus_NameAndSubStatus_Name(String statusName, String subStatusName);

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
                CAST(:active AS boolean) IS NULL
                OR c.is_active = CAST(:active AS boolean)
            )

            AND (
                CAST(:statusId AS uuid) IS NULL
                OR c.status_id = CAST(:statusId AS uuid)
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
            @Param("active") Boolean active,
            @Param("statusId") UUID statusId,
            @Param("skills") String skills
    );
}