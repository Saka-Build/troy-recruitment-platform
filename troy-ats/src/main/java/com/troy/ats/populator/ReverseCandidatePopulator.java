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

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ReverseCandidatePopulator {

    private final StatusRepository statusRepository;
    private final SubStatusRepository subStatusRepository;
    private final EmployeeRepository employeeRepository;


    public void populate(CandidateCreateRequest source, Candidate target) {

        if(Objects.nonNull(source.getFullName()))
        {
            target.setFullName(source.getFullName());
        }
        if(Objects.nonNull(source.getEmail()))
        {
            target.setEmail(source.getEmail());
        }
        if(Objects.nonNull(source.getPhone()))
        {
            target.setPhone(source.getPhone());
        }
        if(Objects.nonNull(source.getWhatsapp()))
        {
            target.setWhatsapp(source.getWhatsapp());
        }
        if(Objects.nonNull(source.getLocation()))
        {
            target.setLocation(source.getLocation());
        }
        if(Objects.nonNull(source.getNationality()))
        {
            target.setNationality(source.getNationality());
        }
        if(Objects.nonNull(source.getCurrentDesignation()))
        {
            target.setCurrentDesignation(source.getCurrentDesignation());
        }
        if(Objects.nonNull(source.getCurrentEmployer()))
        {
            target.setCurrentEmployer(source.getCurrentEmployer());
        }
        if(Objects.nonNull(source.getExperienceYears()))
        {
            target.setExperienceYears(source.getExperienceYears());
        }
        if(Objects.nonNull(source.getNoticePeriodDays()))
        {
            target.setNoticePeriodDays(source.getNoticePeriodDays());
        }
        if(Objects.nonNull(source.getCurrentSalary()))
        {
            target.setCurrentSalary(source.getCurrentSalary());
        }
        if(Objects.nonNull(source.getExpectedSalary()))
        {
            target.setExpectedSalary(source.getExpectedSalary());
        }
        if(Objects.nonNull(source.getSalaryCurrency()))
        {
            target.setSalaryCurrency(source.getSalaryCurrency());
        }
        if(Objects.nonNull(source.getSkills()))
        {
            target.setSkills(source.getSkills());
        }
        if(Objects.nonNull(source.getEducation()))
        {
            target.setEducation(source.getEducation());
        }
        if(Objects.nonNull(source.getVisaStatus()))
        {
            target.setVisaStatus(source.getVisaStatus());
        }
        if(Objects.nonNull(source.getLinkedinUrl()))
        {
            target.setLinkedinUrl(source.getLinkedinUrl());
        }
        if(Objects.nonNull(source.getSource()))
        {
            target.setSource(source.getSource());
        }
        if(Objects.nonNull(source.getReferredBy()))
        {
            target.setReferredBy(source.getReferredBy());
        }
        if(Objects.nonNull(source.getReferenceNote()))
        {
            target.setReferenceNote(source.getReferenceNote());
        }

        // Status
        if (source.getStatusId() != null) {
            Status status = statusRepository.findById(source.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Status not found"));

            target.setStatus(status);
        }

        // Sub status
        if (source.getSubStatusId() != null) {
            SubStatus subStatus = subStatusRepository.findById(source.getSubStatusId())
                    .orElseThrow(() -> new RuntimeException("Sub status not found"));

            target.setSubStatus(subStatus);
        }

        // CV owner
        if (source.getCvOwnerId() != null) {
            Employee employee = employeeRepository.findById(source.getCvOwnerId())
                    .orElseThrow(() -> new RuntimeException("CV owner not found"));

            target.setCvOwner(employee);
        }

        if(Objects.nonNull(source.getCvId()))
        {
            target.setCvId(source.getCvId());
        }
        if(Objects.nonNull(source.isActive()))
        {
            target.setActive(source.isActive());
        }

    }

}