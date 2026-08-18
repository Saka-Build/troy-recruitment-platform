package com.troy.ats.populator;

import com.troy.ats.dto.JobDto;
import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.JobType;
import com.troy.ats.enums.JobWorkMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Component
public class JobPopulator {


    public void populate(Job source, JobDto target) {

        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setTitle(source.getTitle());

        // Client
        if (source.getClient() != null) {
            target.setClientId(source.getClient().getId());
            target.setClientName(source.getClient().getName());
        }

        target.setLocation(source.getLocation());

        // Country
        if (source.getCountry() != null) {
            target.setCountryId(source.getCountry().getId());
            target.setCountryCode(source.getCountry().getCode());
            target.setCountryName(source.getCountry().getName());
        }

        target.setWorkMode(source.getWorkMode());
        target.setJobType(source.getJobType());
        target.setIndustry(source.getIndustry());

        target.setExperienceMin(source.getExperienceMin());
        target.setExperienceMax(source.getExperienceMax());

        target.setSalaryMin(source.getSalaryMin());
        target.setSalaryMax(source.getSalaryMax());
        target.setSalaryCurrency(source.getSalaryCurrency());

        target.setSkillsRequired(source.getSkillsRequired());

        target.setStatus(source.getStatus());
        target.setPriority(source.getPriority());

        target.setDescription(source.getDescription());
        target.setDescriptionSource(source.getDescriptionSource());

        target.setIsTemplate(source.getIsTemplate());
        target.setTemplateName(source.getTemplateName());

        target.setAtsKeywords(source.getAtsKeywords());

        target.setOpeningsCount(source.getOpeningsCount());
        target.setFilledCount(source.getFilledCount());

        // Owner
        if (source.getOwner() != null) {
            target.setOwnerId(source.getOwner().getId());
            target.setOwnerName(source.getOwner().getFullName());
        }

    }

}