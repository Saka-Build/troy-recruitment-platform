package com.troy.ats.populator;

import com.troy.ats.dto.JobCreateRequest;
import com.troy.ats.entity.*;
import com.troy.ats.enums.*;
import com.troy.ats.service.ClientService;
import com.troy.ats.service.EndClientService;
import com.troy.ats.service.impl.CountryServiceImpl;
import com.troy.ats.service.impl.EmployeeServiceImpl;
import com.troy.ats.service.impl.JobServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;


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

        if (source.getTitle() != null) {
            target.setTitle(source.getTitle());
        }

        if (source.getLocation() != null) {
            target.setLocation(source.getLocation());
        }

        if (source.getWorkMode() != null) {
            target.setWorkMode(JobWorkMode.fromValue(source.getWorkMode()));
        }

        if (source.getJobType() != null) {
            target.setJobType(JobType.fromValue(source.getJobType()));
        }

        if (source.getIndustry() != null) {
            target.setIndustry(source.getIndustry());
        }

        if (source.getExperienceMin() != null) {
            target.setExperienceMin(source.getExperienceMin());
        }

        if (source.getExperienceMax() != null) {
            target.setExperienceMax(source.getExperienceMax());
        }

        if (source.getSalaryMin() != null) {
            target.setSalaryMin(source.getSalaryMin());
        }

        if (source.getSalaryMax() != null) {
            target.setSalaryMax(source.getSalaryMax());
        }

        if (source.getSalaryCurrency() != null) {
            target.setSalaryCurrency(source.getSalaryCurrency());
        }

        if (source.getSkillsRequired() != null) {
            target.setSkillsRequired(source.getSkillsRequired());
        }
        if (source.getStatus() != null) {
            target.setStatus(JobStatus.fromValue(source.getStatus()));
        }

        if (source.getPriority() != null) {
            target.setPriority(source.getPriority().toLowerCase(Locale.ROOT));
        }

        if (source.getLeadNote() != null) {
            target.setLeadNote(source.getLeadNote());
        }

        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }

        if (source.getDescriptionSource() != null) {
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
            target.setClientRateAmount(source.getClientRateAmount());
        }
        if (source.getClientRateCurrency() != null) {
            target.setClientRateCurrency(Currency.fromValue(source.getClientRateCurrency()));
        }
        if (source.getClientRatePeriod() != null) {
            target.setClientRatePeriod(RatePeriod.fromValue(source.getClientRatePeriod()));
        }

        if (source.getCandidateRateAmount() != null) {
            target.setCandidateRateAmount(source.getCandidateRateAmount());
        }
        if (source.getCandidateRateCurrency() != null) {
            target.setCandidateRateCurrency(Currency.fromValue(source.getCandidateRateCurrency()));
        }
        if (source.getCandidateRatePeriod() != null) {
            target.setCandidateRatePeriod(RatePeriod.fromValue(source.getCandidateRatePeriod()));
        }

        // Don't overwrite filledCount during normal create/update
        if (target.getFilledCount() == null) {
            target.setFilledCount((short) 0);
        }

        // client
        if (source.getClientId() != null) {
            Client client = clientService.getClientById(source.getClientId());
            target.setClient(client);
        }
        //end client
        if (source.getEndClientId() != null) {
            EndClient endClient = endClientService.getEndClientById(source.getEndClientId());
            target.setEndClient(endClient);
        }
        // country
        if (source.getCountryCode() != null) {
            Country country = countryService.getCountryByCode(source.getCountryCode());
            target.setCountry(country);

        }
        // cowner
        if (source.getOwnerId() != null) {
            Employee employee = employeeService.getEmployeeById(source.getOwnerId());
            target.setOwner(employee);
        }
        //assigned Recruiters
        if(Objects.nonNull(source.getAssignedRecruiters())){
            target.setAssignedRecruiters(source.getAssignedRecruiters());
        }

    }


}