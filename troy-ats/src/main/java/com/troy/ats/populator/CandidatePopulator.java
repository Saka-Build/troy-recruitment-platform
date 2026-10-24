package com.troy.ats.populator;

import com.troy.ats.dto.CandidateDto;
import com.troy.ats.dto.CandidateHeaderDto;
import com.troy.ats.dto.CandidateProfileDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.enums.CvFormat;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static com.troy.ats.util.CommonUtil.enumToStringFormat;


@Component
public class CandidatePopulator {


    public void populate(Candidate source, CandidateDto target) {

        target.setId(source.getId());
        target.setCvId(source.getCvId());
        target.setFullName(source.getFullName());
        target.setCurrentDesignation(source.getCurrentDesignation());
        if(Objects.nonNull(source.getCvOwner())){
            target.setCvOwnerId(source.getCvOwner().getId());
            target.setCvOwnerName(source.getCvOwner().getFullName());
        }
        target.setReferredBy(source.getReferredBy());
        target.setReferenceNote(source.getReferenceNote());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());
        target.setLocation(source.getLocation());
        target.setNationality(source.getNationality());
        target.setCurrentEmployer(source.getCurrentEmployer());
        target.setExperienceYears(source.getExperienceYears());
        target.setSkills(source.getSkills());
        target.setNoticePeriodDays(source.getNoticePeriodDays());
        target.setVisaStatus(source.getVisaStatus());
        target.setSource(source.getSource());
        target.setLinkedinUrl(source.getLinkedinUrl());
        if(source.getStatus() != null){
            target.setStatus(enumToStringFormat(source.getStatus().name()));
        }

        target.setEducation(source.getEducation());
        if(source.getCurrentSalaryAmount() != null){
            target.setCurrentSalaryAmount(source.getCurrentSalaryAmount());
        }
        if(source.getCurrentSalaryCurrency() != null){
            target.setCurrentSalaryCurrency(source.getCurrentSalaryCurrency().name());
        }
        if(source.getCurrentSalaryPeriod() != null){
            target.setCurrentSalaryPeriod(source.getCurrentSalaryPeriod().name().toLowerCase(Locale.ROOT));
        }

        target.setExpectedSalaryAmount(source.getExpectedSalaryAmount());
        if(source.getExpectedSalaryCurrency() != null){
            target.setExpectedSalaryCurrency(source.getExpectedSalaryCurrency().name());
        }
        if(source.getExpectedSalaryPeriod() != null){
            target.setExpectedSalaryPeriod(source.getExpectedSalaryPeriod().name().toLowerCase(Locale.ROOT));
        }

        target.setOriginalCvUrl(source.getOriginalCvUrl());
        target.setOriginalCvFormat(source.getOriginalCvFormat());
        target.setTroyCvUrl(source.getTroyCvUrl());
        target.setCreatedAt(source.getCreatedAt());

    }

}