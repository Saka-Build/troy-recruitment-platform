package com.troy.ats.populator;

import com.troy.ats.dto.CandidateCreateRequest;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.repository.EmployeeRepository;
import com.troy.ats.repository.StatusRepository;
import com.troy.ats.repository.SubStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReverseCandidatePopulator {

    private final StatusRepository statusRepository;
    private final SubStatusRepository subStatusRepository;
    private final EmployeeRepository employeeRepository;


    public void populate(CandidateCreateRequest source, Candidate target) {

        target.setFullName(source.getFullName());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());
        target.setLocation(source.getLocation());
        target.setNationality(source.getNationality());

        target.setCurrentDesignation(source.getCurrentDesignation());
        target.setCurrentEmployer(source.getCurrentEmployer());
        target.setExperienceYears(source.getExperienceYears());
        target.setNoticePeriodDays(source.getNoticePeriodDays());
        target.setCurrentSalary(source.getCurrentSalary());
        target.setExpectedSalary(source.getExpectedSalary());
        target.setSalaryCurrency(source.getSalaryCurrency());

        target.setSkills(source.getSkills());
        target.setEducation(source.getEducation());
        target.setVisaStatus(source.getVisaStatus());
        target.setLinkedinUrl(source.getLinkedinUrl());
        target.setSource(source.getSource());
        target.setReferredBy(source.getReferredBy());
        target.setReferenceNote(source.getReferenceNote());

        // Status
        if (source.getStatusId() != null) {
            Status status = statusRepository
                    .findById(source.getStatusId())
                    .orElseThrow(() ->
                            new RuntimeException("Status not found"));

            target.setStatus(status);
        }

        // Sub status
        if (source.getSubStatusId() != null) {
            SubStatus subStatus = subStatusRepository
                    .findById(source.getSubStatusId())
                    .orElseThrow(() ->
                            new RuntimeException("Sub status not found"));

            target.setSubStatus(subStatus);
        }

        // CV owner
        if (source.getCvOwnerId() != null) {
            Employee employee = employeeRepository
                    .findById(source.getCvOwnerId())
                    .orElseThrow(() ->
                            new RuntimeException("CV owner not found"));

            target.setCvOwner(employee);
        }

        // Generate CV ID
        target.setCvId(source.getCvId());

    }

}