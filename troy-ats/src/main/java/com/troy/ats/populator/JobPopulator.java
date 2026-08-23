package com.troy.ats.populator;

import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.EndClientDto;
import com.troy.ats.dto.JobDto;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.EndClient;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.JobType;
import com.troy.ats.enums.JobWorkMode;
import com.troy.ats.service.impl.EmployeeServiceImpl;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.troy.ats.util.CommonUtil.enumToStringFormat;


@Component
public class JobPopulator {


    private final EmployeeServiceImpl employeeService;
    private final EmployeePopulator employeePopulator;

    public JobPopulator(EmployeeServiceImpl employeeService, EmployeePopulator employeePopulator) {
        this.employeeService = employeeService;
        this.employeePopulator = employeePopulator;
    }

    public void populate(Job source, JobDto target) {

        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setJobId(source.getJobId());
        target.setTitle(source.getTitle());

        // Client
        if (source.getClient() != null) {
            target.setClientId(source.getClient().getId());
            target.setClientName(source.getClient().getName());
        }
        // end Client
        if (source.getEndClient() != null) {
            target.setEndClientId(source.getEndClient().getId());
            target.setEndClientName(source.getEndClient().getName());
        }

        target.setLocation(source.getLocation());

        // Country
        if (source.getCountry() != null) {
            target.setCountryId(source.getCountry().getId());
            target.setCountryCode(source.getCountry().getCode());
            target.setCountryName(source.getCountry().getName());
        }

        target.setWorkMode(enumToStringFormat(source.getWorkMode().name()));
        target.setJobType(enumToStringFormat(source.getJobType().name()));
        target.setIndustry(source.getIndustry());

        target.setExperienceMin(source.getExperienceMin());
        target.setExperienceMax(source.getExperienceMax());

        target.setSkillsRequired(source.getSkillsRequired());

        target.setStatus(enumToStringFormat(source.getStatus().name()));
        target.setPriority(enumToStringFormat(source.getPriority()));

        target.setLeadNote(source.getLeadNote());
        target.setDescription(source.getDescription());
        target.setDescriptionSource(source.getDescriptionSource());

        target.setIsTemplate(source.getIsTemplate());
        target.setTemplateName(source.getTemplateName());

        target.setClientRateAmount(source.getClientRateAmount());
        target.setClientRateCurrency(source.getClientRateCurrency().name());
        target.setClientRatePeriod(source.getClientRatePeriod().name().toLowerCase(Locale.ROOT));
        target.setCandidateRateAmount(source.getCandidateRateAmount());
        target.setCandidateRateCurrency(source.getCandidateRateCurrency().name());
        target.setCandidateRatePeriod(source.getCandidateRatePeriod().name().toLowerCase(Locale.ROOT));

        // Owner
        if (source.getOwner() != null) {
            target.setOwnerId(source.getOwner().getId());
            target.setOwnerName(source.getOwner().getFullName());
        }

        populateAssignedRecruiters(source, target);

       /* target.setAtsKeywords(source.getAtsKeywords());
        target.setOpeningsCount(source.getOpeningsCount());
        target.setFilledCount(source.getFilledCount());
        target.setSalaryMin(source.getSalaryMin());
        target.setSalaryMax(source.getSalaryMax());
        target.setSalaryCurrency(source.getSalaryCurrency());*/

    }

    private void  populateAssignedRecruiters(Job source, JobDto target){

        if (source.getAssignedRecruiters() == null || source.getAssignedRecruiters().length == 0) {

            target.setAssignedRecruiters(Collections.emptyList());
            return;
        }
        List<Employee> employees = employeeService.getEmployeesByIds( Arrays.asList(source.getAssignedRecruiters()));

        List<EmployeeDto> employeeDtos =  employees.stream().map(employee -> {
            EmployeeDto employeeDto = new EmployeeDto();
            employeePopulator.populate(employee, employeeDto);
            return employeeDto;
        }).toList();

        target.setAssignedRecruiters(employeeDtos);
    }

}