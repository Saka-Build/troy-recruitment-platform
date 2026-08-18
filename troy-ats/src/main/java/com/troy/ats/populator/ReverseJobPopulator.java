package com.troy.ats.populator;

import com.troy.ats.dto.JobCreateRequest;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.Country;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.service.ClientService;
import com.troy.ats.service.JobService;
import com.troy.ats.service.impl.CountryServiceImpl;
import com.troy.ats.service.impl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReverseJobPopulator {

    private final ClientService clientService;
    private final CountryServiceImpl countryService;
    private final EmployeeServiceImpl employeeService;

    public void populate(JobCreateRequest source, Job target) {

        if (source == null || target == null) {
            return;
        }

        target.setTitle(source.getTitle());
        target.setLocation(source.getLocation());

        target.setWorkMode(source.getWorkMode());
        target.setJobType(source.getJobType());

        target.setIndustry(source.getIndustry());

        target.setExperienceMin(source.getExperienceMin());
        target.setExperienceMax(source.getExperienceMax());

        target.setSalaryMin(source.getSalaryMin());
        target.setSalaryMax(source.getSalaryMax());
        target.setSalaryCurrency(source.getSalaryCurrency());

        target.setSkillsRequired(source.getSkillsRequired());
        target.setStatus(source.getStatus() != null ? source.getStatus() : JobStatus.OPEN);
        target.setPriority(source.getPriority());

        target.setDescription(source.getDescription());
        target.setDescriptionSource(source.getDescriptionSource());

        target.setIsTemplate(
                source.getIsTemplate() != null
                        ? source.getIsTemplate()
                        : false
        );

        target.setTemplateName(source.getTemplateName());

        target.setAtsKeywords(
                source.getAtsKeywords() != null
                        ? source.getAtsKeywords()
                        : new String[0]
        );

        target.setOpeningsCount(
                source.getOpeningsCount() != null
                        ? source.getOpeningsCount()
                        : (short) 1
        );

        // Don't overwrite filledCount during normal create/update
        if (target.getFilledCount() == null) {
            target.setFilledCount((short) 0);
        }

        // client
        if (source.getClientId() != null) {
            Client client = clientService.getClientById(source.getClientId());
               target.setClient(client);

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
    }


}