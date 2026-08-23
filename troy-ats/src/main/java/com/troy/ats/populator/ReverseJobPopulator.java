package com.troy.ats.populator;

import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.dto.JobCreateRequest;
import com.troy.ats.entity.*;
import com.troy.ats.enums.*;
import com.troy.ats.enums.Currency;
import com.troy.ats.service.ClientService;
import com.troy.ats.service.EndClientService;
import com.troy.ats.service.impl.CountryServiceImpl;
import com.troy.ats.service.impl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.troy.ats.util.CommonUtil.getFieldName;
import static com.troy.ats.util.CommonUtil.populateActivityLog;


@Component
@RequiredArgsConstructor
public class ReverseJobPopulator {

    private final ClientService clientService;
    private final EndClientService endClientService;
    private final CountryServiceImpl countryService;
    private final EmployeeServiceImpl employeeService;

    public void populate(JobCreateRequest source, Job target) {

        if (source == null || target == null) {
            return;
        }

        List<ActivityLogRequest> activityLogs = new ArrayList<>();
        String entityType = target.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        UUID entityId = target.getId();

        if (source.getTitle() != null) {
            String oldValue = Objects.nonNull(target.getTitle()) ? target.getTitle() : null;
            String field = getFieldName(Job.class, "setTitle", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getTitle(),activityLogs);
            target.setTitle(source.getTitle());
        }

        if (source.getLocation() != null) {
            String oldValue = Objects.nonNull(target.getLocation()) ? target.getLocation() : null;
            String field = getFieldName(Job.class, "setLocation", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getLocation(),activityLogs);
            target.setLocation(source.getLocation());
        }

        if (source.getWorkMode() != null) {
            String oldValue = Objects.nonNull(target.getWorkMode()) ? target.getWorkMode().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Job.class, "setWorkMode", JobWorkMode.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getWorkMode(),activityLogs);
            target.setWorkMode(JobWorkMode.fromValue(source.getWorkMode()));
        }

        if (source.getJobType() != null) {
            String oldValue = Objects.nonNull(target.getJobType()) ? target.getJobType().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Job.class, "setJobType", JobType.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getJobType(),activityLogs);
            target.setJobType(JobType.fromValue(source.getJobType()));
        }

        if (source.getIndustry() != null) {
            String oldValue = Objects.nonNull(target.getIndustry()) ? target.getIndustry() : null;
            String field = getFieldName(Job.class, "setIndustry", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getIndustry(),activityLogs);
            target.setIndustry(source.getIndustry());
        }

        if (source.getExperienceMin() != null) {
            String oldValue = Objects.nonNull(target.getExperienceMin()) ? target.getExperienceMin().toString() : null;
            String field = getFieldName(Job.class, "setExperienceMin", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getExperienceMin().toString(),activityLogs);
            target.setExperienceMin(source.getExperienceMin());
        }

        if (source.getExperienceMax() != null) {
            String oldValue = Objects.nonNull(target.getExperienceMax()) ? target.getExperienceMax().toString() : null;
            String field = getFieldName(Job.class, "setExperienceMax",  BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getExperienceMax().toString(),activityLogs);
            target.setExperienceMax(source.getExperienceMax());
        }

        if (source.getSalaryMin() != null) {
            String oldValue = Objects.nonNull(target.getSalaryMin()) ? target.getSalaryMin().toString() : null;
            String field = getFieldName(Job.class, "setSalaryMin", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getSalaryMin().toString(),activityLogs);
            target.setSalaryMin(source.getSalaryMin());
        }

        if (source.getSalaryMax() != null) {
            String oldValue = Objects.nonNull(target.getSalaryMax()) ? target.getSalaryMax().toString() : null;
            String field = getFieldName(Job.class, "setSalaryMax", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getSalaryMax().toString(),activityLogs);
            target.setSalaryMax(source.getSalaryMax());
        }

        if (source.getSalaryCurrency() != null) {
            String oldValue = Objects.nonNull(target.getSalaryCurrency()) ? target.getSalaryCurrency() : null;
            String field = getFieldName(Job.class, "setSalaryCurrency", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getSalaryCurrency(),activityLogs);
            target.setSalaryCurrency(source.getSalaryCurrency());
        }

        if (source.getSkillsRequired() != null) {
            target.setSkillsRequired(source.getSkillsRequired());
        }
        if (source.getStatus() != null) {
            String oldValue = Objects.nonNull(target.getStatus()) ? target.getStatus().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Job.class, "setStatus", JobStatus.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getStatus(),activityLogs);
            target.setStatus(JobStatus.fromValue(source.getStatus()));
        }

        if (source.getPriority() != null) {
            String oldValue = Objects.nonNull(target.getPriority()) ? target.getPriority() : null;
            String field = getFieldName(Job.class, "setPriority", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getPriority().toLowerCase(Locale.ROOT),activityLogs);
            target.setPriority(source.getPriority().toLowerCase(Locale.ROOT));
        }

        if (source.getLeadNote() != null) {
            String oldValue = Objects.nonNull(target.getLeadNote()) ? target.getLeadNote() : null;
            String field = getFieldName(Job.class, "setLeadNote", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getLeadNote(),activityLogs);
            target.setLeadNote(source.getLeadNote());
        }

        if (source.getDescription() != null) {
            String oldValue = Objects.nonNull(target.getDescription()) ? target.getDescription() : null;
            String field = getFieldName(Job.class, "setDescription", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getDescription(),activityLogs);
            target.setDescription(source.getDescription());
        }

        if (source.getDescriptionSource() != null) {
            String oldValue = Objects.nonNull(target.getDescriptionSource()) ? target.getDescriptionSource() : null;
            String field = getFieldName(Job.class, "setDescriptionSource", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getDescriptionSource(),activityLogs);
            target.setDescriptionSource(source.getDescriptionSource());
        }

        if (source.getIsTemplate() != null) {
            target.setIsTemplate(source.getIsTemplate());
        }

        if (source.getTemplateName() != null) {
            target.setTemplateName(source.getTemplateName());
        }

        if (source.getAtsKeywords() != null) {
            target.setAtsKeywords(source.getAtsKeywords());
        }

        if (source.getOpeningsCount() != null) {
            target.setOpeningsCount(source.getOpeningsCount());
        }

        if (source.getClientRateAmount() != null) {
            String oldValue = Objects.nonNull(target.getClientRateAmount()) ? target.getClientRateAmount().toString() : null;
            String field = getFieldName(Job.class, "setClientRateAmount", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getClientRateAmount().toString(),activityLogs);
            target.setClientRateAmount(source.getClientRateAmount());
        }
        if (source.getClientRateCurrency() != null) {
            String oldValue = Objects.nonNull(target.getClientRateCurrency()) ? target.getClientRateCurrency().name() : null;
            String field = getFieldName(Job.class, "setClientRateCurrency", Currency.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getClientRateCurrency(),activityLogs);
            target.setClientRateCurrency(Currency.fromValue(source.getClientRateCurrency()));
        }
        if (source.getClientRatePeriod() != null) {
            String oldValue = Objects.nonNull(target.getClientRatePeriod()) ? target.getClientRatePeriod().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Job.class, "setClientRatePeriod", RatePeriod.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getClientRatePeriod(),activityLogs);
            target.setClientRatePeriod(RatePeriod.fromValue(source.getClientRatePeriod()));
        }

        if (source.getCandidateRateAmount() != null) {
            String oldValue = Objects.nonNull(target.getCandidateRateAmount()) ? target.getCandidateRateAmount().toString() : null;
            String field = getFieldName(Job.class, "setCandidateRateAmount", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCandidateRateAmount().toString(),activityLogs);
            target.setCandidateRateAmount(source.getCandidateRateAmount());
        }
        if (source.getCandidateRateCurrency() != null) {
            String oldValue = Objects.nonNull(target.getCandidateRateCurrency()) ? target.getCandidateRateCurrency().name() : null;
            String field = getFieldName(Job.class, "setCandidateRateCurrency", Currency.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCandidateRateCurrency(),activityLogs);
            target.setCandidateRateCurrency(Currency.fromValue(source.getCandidateRateCurrency()));
        }
        if (source.getCandidateRatePeriod() != null) {
            String oldValue = Objects.nonNull(target.getCandidateRatePeriod()) ? target.getCandidateRatePeriod().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Job.class, "setCandidateRatePeriod", RatePeriod.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCandidateRatePeriod(),activityLogs);
            target.setCandidateRatePeriod(RatePeriod.fromValue(source.getCandidateRatePeriod()));
        }

        // Don't overwrite filledCount during normal create/update
        if (target.getFilledCount() == null) {
            target.setFilledCount((short) 0);
        }

        // client
        if (source.getClientId() != null) {
            Client client = clientService.getClientById(source.getClientId());
            String oldValue = Objects.nonNull(target.getClient()) ? target.getClient().getName(): null;
            String field = getFieldName(Job.class, "setClient",  Client.class);
            populateActivityLog(entityType, entityId, field, oldValue,client.getName(),activityLogs);
            target.setClient(client);
        }
        //end client
        if (source.getEndClientId() != null) {
            EndClient endClient = endClientService.getEndClientById(source.getEndClientId());
            String oldValue = Objects.nonNull(target.getEndClient()) ? target.getEndClient().getName(): null;
            String field = getFieldName(Job.class, "setEndClient",  EndClient.class);
            populateActivityLog(entityType, entityId, field, oldValue,endClient.getName(),activityLogs);
            target.setEndClient(endClient);
        }
        // country
        if (source.getCountryCode() != null) {
            Country country = countryService.getCountryByCode(source.getCountryCode());
            String oldValue = Objects.nonNull(target.getCountry()) ? target.getCountry().getName(): null;
            String field = getFieldName(Job.class, "setCountry", Country.class);
            populateActivityLog(entityType, entityId, field, oldValue,country.getName(),activityLogs);
            target.setCountry(country);

        }
        // cowner
        if (source.getOwnerId() != null) {
            Employee employee = employeeService.getEmployeeById(source.getOwnerId());
            String oldValue = Objects.nonNull(target.getOwner()) ? target.getOwner().getFullName(): null;
            String field = getFieldName(Job.class, "setOwner", Employee.class);
            populateActivityLog(entityType, entityId, field, oldValue,employee.getFullName(),activityLogs);
            target.setOwner(employee);
        }
        //assigned Recruiters
        if(Objects.nonNull(source.getAssignedRecruiters())){
            target.setAssignedRecruiters(source.getAssignedRecruiters());
        }

        source.setActivityLogs(activityLogs);

    }


}