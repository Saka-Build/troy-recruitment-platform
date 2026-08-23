package com.troy.ats.controller;

import com.troy.ats.dto.ActivityLogDto;
import com.troy.ats.dto.DashboardSummaryDto;
import com.troy.ats.service.ActivityLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/activityLog")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @GetMapping("/{entityType}/{entityId}")
    public ResponseEntity<List<ActivityLogDto>> getActivityLogs(
                                                 @PathVariable String entityType,
                                                 @PathVariable UUID entityId) {

        return ResponseEntity.ok(activityLogService.findByEntityTypeAndEntityId(entityType, entityId));
    }

}