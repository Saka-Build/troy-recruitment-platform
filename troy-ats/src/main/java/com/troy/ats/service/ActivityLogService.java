package com.troy.ats.service;


import com.troy.ats.dto.ActivityLogDto;
import com.troy.ats.entity.ActivityLog;
import com.troy.ats.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface ActivityLogService {

    /**
     *
     * @param entityType
     * @param entityId
     * @param action
     * @param oldValue
     * @param newValue
     * @param description
     * @param performedBy
     * @return
     */
    ActivityLog save(
            String entityType,
            UUID entityId,
            String action,
            String oldValue,
            String newValue,
            String description,
            Employee performedBy
    );

    /**
     *
     * @param entityType
     * @param entityId
     * @return
     */
    List<ActivityLogDto>  findByEntityTypeAndEntityId(String entityType, UUID entityId);

    /**
     *
     * @param entityType
     * @param entityId
     * @return
     */
    long countByEntityTypeAndEntityId(String entityType, UUID entityId);

    /**
     *
     * @param activityLogs
     * @return
     */
    List<ActivityLog> saveAll(List<ActivityLog> activityLogs);

}