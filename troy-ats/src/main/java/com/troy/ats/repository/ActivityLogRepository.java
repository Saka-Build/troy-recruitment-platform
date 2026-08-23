package com.troy.ats.repository;

import com.troy.ats.entity.ActivityLog;
import com.troy.ats.entity.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID>, JpaSpecificationExecutor<ActivityLog> {

    @EntityGraph(attributePaths = {"performedBy"})
    List<ActivityLog> findByEntityTypeAndEntityIdOrderByPerformedAtDesc(String entityType, UUID entityId);

    long countByEntityTypeAndEntityId(String entityType, UUID entityId);
}