package com.troy.ats.populator;

import com.troy.ats.dto.CandidateCreateRequest;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.enums.CandidateStatus;
import com.troy.ats.enums.Currency;
import com.troy.ats.enums.RatePeriod;
import com.troy.ats.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ReverseCandidatePopulator {

    private final EmployeeRepository employeeRepository;


    public void populate(CandidateCreateRequest source, Candidate target) {


        if (source.getFullName() != null) {
            target.setFullName(source.getFullName());
        }

        if (source.getCurrentDesignation() != null) {
            target.setCurrentDesignation(source.getCurrentDesignation());
        }

        if (source.getReferredBy() != null) {
            target.setReferredBy(source.getReferredBy());
        }

        if (source.getReferenceNote() != null) {
            target.setReferenceNote(source.getReferenceNote());
        }

        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }

        if (source.getPhone() != null) {
            target.setPhone(source.getPhone());
        }

        if (source.getWhatsapp() != null) {
            target.setWhatsapp(source.getWhatsapp());
        }

        if (source.getLocation() != null) {
            target.setLocation(source.getLocation());
        }

        if (source.getNationality() != null) {
            target.setNationality(source.getNationality());
        }

        if (source.getCurrentEmployer() != null) {
            target.setCurrentEmployer(source.getCurrentEmployer());
        }

        if (source.getExperienceYears() != null) {
            target.setExperienceYears(source.getExperienceYears());
        }

        if (source.getSkills() != null) {
            target.setSkills(source.getSkills());
        }

        if (source.getNoticePeriodDays() != null) {
            target.setNoticePeriodDays(source.getNoticePeriodDays());
        }

        if (source.getVisaStatus() != null) {
            target.setVisaStatus(source.getVisaStatus());
        }

        if (source.getSource() != null) {
            target.setSource(source.getSource());
        }

        if (source.getLinkedinUrl() != null) {
            target.setLinkedinUrl(source.getLinkedinUrl());
        }

        if (source.getStatus() != null) {
            target.setStatus(CandidateStatus.fromValue(source.getStatus()));
        }

        if (source.getEducation() != null) {
            target.setEducation(source.getEducation());
        }

        if (source.getCurrentSalaryAmount() != null) {
            target.setCurrentSalaryAmount(source.getCurrentSalaryAmount());
        }

        if (source.getCurrentSalaryCurrency() != null) {
            target.setCurrentSalaryCurrency(Currency.fromValue(source.getCurrentSalaryCurrency()));
        }

        if (source.getCurrentSalaryPeriod() != null) {
            target.setCurrentSalaryPeriod(RatePeriod.fromValue(source.getCurrentSalaryPeriod()));
        }

        if (source.getExpectedSalaryAmount() != null) {
            target.setExpectedSalaryAmount(source.getExpectedSalaryAmount());
        }

        if (source.getExpectedSalaryCurrency() != null) {
            target.setExpectedSalaryCurrency(Currency.fromValue(source.getExpectedSalaryCurrency()));
        }

        if (source.getExpectedSalaryPeriod() != null) {
            target.setExpectedSalaryPeriod(RatePeriod.fromValue(source.getExpectedSalaryPeriod()));
        }

        // CV owner
        if (source.getCvOwnerId() != null) {
            Employee employee = employeeRepository.findById(source.getCvOwnerId())
                    .orElseThrow(() -> new RuntimeException("CV owner not found"));

            target.setCvOwner(employee);
        }

        /*if(Objects.nonNull(source.getCvId()))
        {
            target.setCvId(source.getCvId());
        }*/


    }

}