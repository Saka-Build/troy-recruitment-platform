package com.troy.ats.populator;

import com.troy.ats.constants.CommonConstants;
import com.troy.ats.dto.DashboardSummaryDto;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.InterviewDataForDashboardDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Interview;
import com.troy.ats.enums.CountryEnum;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.OfferStatus;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.service.*;
import com.troy.ats.util.CommonUtil;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeePopulator {

    @Autowired
    @Resource(name="sessionService")
    private SessionService sessionService;

    public void populate(Employee source, EmployeeDto target) {

        target.setId(source.getId());
        target.setEmployeeCode(source.getEmployeeCode());
        target.setFullName(source.getFullName());
        target.setDesignation(source.getDesignation());
        target.setOfficialEmail(source.getOfficialEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());
        target.setRole(source.getRole());
        target.setLastLoginAt(convertInstantToLocalDate(source.getLastLoginAt()));

    }

    private LocalDateTime convertInstantToLocalDate(Instant lastLoginAt){

        ZoneId zoneId = CommonUtil.getZoneIdForCurrentUser(sessionService);
        ZonedDateTime DateTime = lastLoginAt.atZone(zoneId);
        LocalDateTime date = DateTime.toLocalDateTime();
        return  date;

    }
}
