package com.troy.ats.repository;

import com.troy.ats.constants.CommonConstants;
import com.troy.ats.entity.ActivityLog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID>, JpaSpecificationExecutor<ActivityLog> {

    @EntityGraph(attributePaths = {"performedBy"})
    List<ActivityLog> findByEntityTypeAndEntityIdOrderByPerformedAtDesc(String entityType, UUID entityId);

    long countByEntityTypeAndEntityId(String entityType, UUID entityId);

    @Query(value = """
    SELECT DISTINCT ON (a.entity_id) a.*
    FROM activity_log a
    WHERE a.entity_type = :entityType
      AND a.action = :action
      AND a.entity_id IN (:entityIds)
    ORDER BY a.entity_id, a.performed_at DESC, a.id DESC
    """, nativeQuery = true)
    List<ActivityLog> findLatestByEntityTypeAndAction(
            @Param("entityType") String entityType,
            @Param("action") String action,
            @Param("entityIds") List<UUID> entityIds
    );
}