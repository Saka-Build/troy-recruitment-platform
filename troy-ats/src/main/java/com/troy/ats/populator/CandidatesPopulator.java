package com.troy.ats.populator;

import com.troy.ats.dto.CandidatesDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.entity.Submission;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;


@Component
public class CandidatesPopulator {


    public void populate(Candidate source, CandidatesDto target) {

        target.setId(source.getId());
        target.setFullName(source.getFullName());
        target.setEmail(source.getEmail());
        target.setWhatsapp(source.getWhatsapp());
        target.setLocation(source.getLocation());
        target.setCurrentDesignation(source.getCurrentDesignation());
        target.setExperienceYears(source.getExperienceYears());
        target.setSkills(source.getSkills());
        Status status = source.getStatus();
        if(Objects.nonNull(status)){
            target.setStatusId(status.getId());
            target.setStatusName(status.getName());
            target.setStatusColour(status.getColourHex());
        }
        SubStatus subStatus = source.getSubStatus();
        if(Objects.nonNull(subStatus)){
            target.setSubStatusId(subStatus.getId());
            target.setSubStatusName(subStatus.getName());
        }
        target.setActive(source.getActive());
        target.setCreatedAt(source.getCreatedAt());

    }

}
