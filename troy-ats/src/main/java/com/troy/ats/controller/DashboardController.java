package com.troy.ats.controller;

import com.troy.ats.dto.DashboardSummaryDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.populator.DashBoardSummaryPopulator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashBoardSummaryPopulator dashBoardSummaryPopulator;

    public DashboardController(DashBoardSummaryPopulator dashBoardSummaryPopulator) {
        this.dashBoardSummaryPopulator = dashBoardSummaryPopulator;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDto> getDashBoardSummaryData() {
        DashboardSummaryDto dashboardSummaryDto = new DashboardSummaryDto();
        dashBoardSummaryPopulator.populate(dashboardSummaryDto);
        return ResponseEntity.ok(dashboardSummaryDto);
    }

}