package com.troy.ats.populator;

import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.dto.CandidateCreateRequest;
import com.troy.ats.entity.*;
import com.troy.ats.enums.CandidateStatus;
import com.troy.ats.enums.Currency;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.RatePeriod;
import com.troy.ats.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.troy.ats.util.CommonUtil.getFieldName;
import static com.troy.ats.util.CommonUtil.populateActivityLog;


@Component
@RequiredArgsConstructor
public class ReverseCandidatePopulator {

    private final EmployeeRepository employeeRepository;


    public void populate(CandidateCreateRequest source, Candidate target) {

        List<ActivityLogRequest> activityLogs = new ArrayList<>();
        String entityType = target.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        UUID entityId = target.getId();

        if (source.getFullName() != null) {
            String oldValue = Objects.nonNull(target.getFullName()) ? target.getFullName() : null;
            String field = getFieldName(Candidate.class, "setFullName", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getFullName(),activityLogs);
            target.setFullName(source.getFullName());
        }

        if (source.getCurrentDesignation() != null) {
            String oldValue = Objects.nonNull(target.getCurrentDesignation()) ? target.getCurrentDesignation() : null;
            String field = getFieldName(Candidate.class, "setCurrentDesignation", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCurrentDesignation(),activityLogs);
            target.setCurrentDesignation(source.getCurrentDesignation());
        }

        if (source.getReferredBy() != null) {
            String oldValue = Objects.nonNull(target.getReferredBy()) ? target.getReferredBy() : null;
            String field = getFieldName(Candidate.class, "setReferredBy", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getReferredBy(),activityLogs);
            target.setReferredBy(source.getReferredBy());
        }

        if (source.getReferenceNote() != null) {
            String oldValue = Objects.nonNull(target.getReferenceNote()) ? target.getReferenceNote() : null;
            String field = getFieldName(Candidate.class, "setReferenceNote", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getReferenceNote(),activityLogs);
            target.setReferenceNote(source.getReferenceNote());
        }

        if (source.getEmail() != null) {
            String oldValue = Objects.nonNull(target.getEmail()) ? target.getEmail() : null;
            String field = getFieldName(Candidate.class, "setEmail", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getEmail(),activityLogs);
            target.setEmail(source.getEmail());
        }

        if (source.getPhone() != null) {
            String oldValue = Objects.nonNull(target.getPhone()) ? target.getPhone() : null;
            String field = getFieldName(Candidate.class, "setPhone", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getPhone(),activityLogs);
            target.setPhone(source.getPhone());
        }

        if (source.getWhatsapp() != null) {
            String oldValue = Objects.nonNull(target.getWhatsapp()) ? target.getWhatsapp() : null;
            String field = getFieldName(Candidate.class, "setWhatsapp", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getWhatsapp(),activityLogs);
            target.setWhatsapp(source.getWhatsapp());
        }

        if (source.getLocation() != null) {
            String oldValue = Objects.nonNull(target.getLocation()) ? target.getLocation() : null;
            String field = getFieldName(Candidate.class, "setLocation", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getLocation(),activityLogs);
            target.setLocation(source.getLocation());
        }

        if (source.getNationality() != null) {
            String oldValue = Objects.nonNull(target.getNationality()) ? target.getNationality() : null;
            String field = getFieldName(Candidate.class, "setNationality", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getNationality(),activityLogs);
            target.setNationality(source.getNationality());
        }

        if (source.getCurrentEmployer() != null) {
            String oldValue = Objects.nonNull(target.getCurrentEmployer()) ? target.getCurrentEmployer() : null;
            String field = getFieldName(Candidate.class, "setCurrentEmployer", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCurrentEmployer(),activityLogs);
            target.setCurrentEmployer(source.getCurrentEmployer());
        }

        if (source.getExperienceYears() != null) {
            String oldValue = Objects.nonNull(target.getExperienceYears()) ? target.getExperienceYears().toString() : null;
            String field = getFieldName(Candidate.class, "setExperienceYears", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getExperienceYears().toString(),activityLogs);
            target.setExperienceYears(source.getExperienceYears());
        }

        if (source.getSkills() != null) {
            target.setSkills(source.getSkills());
        }

        if (source.getNoticePeriodDays() != null) {
            String oldValue = Objects.nonNull(target.getNoticePeriodDays()) ? target.getNoticePeriodDays().toString() : null;
            String field = getFieldName(Candidate.class, "setNoticePeriodDays", Short.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getNoticePeriodDays().toString(),activityLogs);
            target.setNoticePeriodDays(source.getNoticePeriodDays());
        }

        if (source.getVisaStatus() != null) {
            String oldValue = Objects.nonNull(target.getVisaStatus()) ? target.getVisaStatus() : null;
            String field = getFieldName(Candidate.class, "setVisaStatus", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getVisaStatus(),activityLogs);
            target.setVisaStatus(source.getVisaStatus());
        }

        if (source.getSource() != null) {
            String oldValue = Objects.nonNull(target.getSource()) ? target.getSource() : null;
            String field = getFieldName(Candidate.class, "setSource", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getSource(),activityLogs);
            target.setSource(source.getSource());
        }

        if (source.getLinkedinUrl() != null) {
            String oldValue = Objects.nonNull(target.getLinkedinUrl()) ? target.getLinkedinUrl() : null;
            String field = getFieldName(Candidate.class, "setLinkedinUrl", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getLinkedinUrl(),activityLogs);
            target.setLinkedinUrl(source.getLinkedinUrl());
        }

        if (source.getStatus() != null) {
            String oldValue = Objects.nonNull(target.getStatus()) ? target.getStatus().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Candidate.class, "setStatus", CandidateStatus.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getStatus(),activityLogs);
            target.setStatus(CandidateStatus.fromValue(source.getStatus()));
        }

        if (source.getEducation() != null) {
            String oldValue = Objects.nonNull(target.getEducation()) ? target.getEducation() : null;
            String field = getFieldName(Candidate.class, "setEducation", String.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getEducation(),activityLogs);
            target.setEducation(source.getEducation());
        }

        if (source.getCurrentSalaryAmount() != null) {
            String oldValue = Objects.nonNull(target.getCurrentSalaryAmount()) ? target.getCurrentSalaryAmount().toString() : null;
            String field = getFieldName(Candidate.class, "setCurrentSalaryAmount", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCurrentSalaryAmount().toString(),activityLogs);
            target.setCurrentSalaryAmount(source.getCurrentSalaryAmount());
        }

        if (source.getCurrentSalaryCurrency() != null) {
            String oldValue = Objects.nonNull(target.getCurrentSalaryCurrency()) ? target.getCurrentSalaryCurrency().name() : null;
            String field = getFieldName(Candidate.class, "setCurrentSalaryCurrency", Currency.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCurrentSalaryCurrency(),activityLogs);
            target.setCurrentSalaryCurrency(Currency.fromValue(source.getCurrentSalaryCurrency()));
        }

        if (source.getCurrentSalaryPeriod() != null) {
            String oldValue = Objects.nonNull(target.getCurrentSalaryPeriod()) ? target.getCurrentSalaryPeriod().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Candidate.class, "setCurrentSalaryPeriod", RatePeriod.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCurrentSalaryPeriod(),activityLogs);
            target.setCurrentSalaryPeriod(RatePeriod.fromValue(source.getCurrentSalaryPeriod()));
        }

        if (source.getExpectedSalaryAmount() != null) {
            String oldValue = Objects.nonNull(target.getExpectedSalaryAmount()) ? target.getExpectedSalaryAmount().toString() : null;
            String field = getFieldName(Candidate.class, "setExpectedSalaryAmount", BigDecimal.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getExpectedSalaryAmount().toString(),activityLogs);
            target.setExpectedSalaryAmount(source.getExpectedSalaryAmount());
        }

        if (source.getExpectedSalaryCurrency() != null) {
            String oldValue = Objects.nonNull(target.getExpectedSalaryCurrency()) ? target.getExpectedSalaryCurrency().name() : null;
            String field = getFieldName(Candidate.class, "setExpectedSalaryCurrency", Currency.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getExpectedSalaryCurrency(),activityLogs);
            target.setExpectedSalaryCurrency(Currency.fromValue(source.getExpectedSalaryCurrency()));
        }

        if (source.getExpectedSalaryPeriod() != null) {
            String oldValue = Objects.nonNull(target.getExpectedSalaryPeriod()) ? target.getExpectedSalaryPeriod().name().toLowerCase(Locale.ROOT) : null;
            String field = getFieldName(Candidate.class, "setExpectedSalaryPeriod", RatePeriod.class);
            populateActivityLog(entityType, entityId, field, oldValue,source.getCurrentSalaryPeriod(),activityLogs);
            target.setExpectedSalaryPeriod(RatePeriod.fromValue(source.getExpectedSalaryPeriod()));
        }

        // CV owner
        if (source.getCvOwnerId() != null) {
            Employee employee = employeeRepository.findById(source.getCvOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("CV owner not found"));

            String oldValue = Objects.nonNull(target.getCvOwner()) ? target.getCvOwner().getFullName(): null;
            String field = getFieldName(Job.class, "setOwner", Employee.class);
            populateActivityLog(entityType, entityId, field, oldValue,employee.getFullName(),activityLogs);

            target.setCvOwner(employee);
        }

        source.setActivityLogs(activityLogs);


        /*if(Objects.nonNull(source.getCvId()))
        {
            target.setCvId(source.getCvId());
        }*/


    }

}