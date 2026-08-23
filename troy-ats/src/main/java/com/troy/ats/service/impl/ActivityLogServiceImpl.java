package com.troy.ats.service.impl;

import com.troy.ats.dto.ActivityLogDto;
import com.troy.ats.dto.ClientDto;
import com.troy.ats.entity.ActivityLog;
import com.troy.ats.entity.Employee;
import com.troy.ats.populator.ActivityLogPopulator;
import com.troy.ats.repository.ActivityLogRepository;
import com.troy.ats.searchfilter.filter.ClientSpecification;
import com.troy.ats.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service("activityLogService")
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogPopulator activityLogPopulator;
    private final ActivityLogRepository activityLogRepository;
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
    @Override
    @Transactional
    public ActivityLog save(String entityType, UUID entityId, String action, String oldValue, String newValue, String description, Employee performedBy) {

        ActivityLog activityLog = ActivityLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .description(description)
                .performedBy(performedBy)
                .performedAt(Instant.now())
                .build();

        return activityLogRepository.save(activityLog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogDto> findByEntityTypeAndEntityId(String entityType, UUID entityId) {

        return activityLogRepository.findByEntityTypeAndEntityIdOrderByPerformedAtDesc(entityType, entityId).stream()
                                    .map(activityLog -> {
                                        ActivityLogDto activityLogDto = new ActivityLogDto();
                                        activityLogPopulator.populate(activityLog, activityLogDto);
                                        return activityLogDto;
                                    }).toList();

    }

    /**
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public long countByEntityTypeAndEntityId(String entityType, UUID entityId) {
        return activityLogRepository.countByEntityTypeAndEntityId(entityType, entityId);
    }

    /**
     *
     * @param activityLogs
     * @return
     */
    @Override
    @Transactional
    public List<ActivityLog> saveAll(List<ActivityLog> activityLogs) {

        return activityLogRepository.saveAll(activityLogs);
    }
}