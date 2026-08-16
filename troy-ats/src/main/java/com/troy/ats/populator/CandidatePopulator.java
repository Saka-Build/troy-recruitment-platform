package com.troy.ats.populator;

import com.troy.ats.dto.CandidateDto;
import com.troy.ats.dto.CandidateHeaderDto;
import com.troy.ats.dto.CandidateProfileDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;


@Component
public class CandidatePopulator {


    public void populate(Candidate source, CandidateDto target) {

        CandidateHeaderDto candidateHeaderDto = new CandidateHeaderDto();
        CandidateProfileDto candidateProfileDto = new CandidateProfileDto();
        populateCandidateHeader(source, candidateHeaderDto);
        populateCandidateProfile(source, candidateProfileDto);

        target.setCandidateHeader(candidateHeaderDto);
        target.setCandidateProfile(candidateProfileDto);
    }

    private void populateCandidateHeader(Candidate source, CandidateHeaderDto target){
        target.setId(source.getId());
        target.setFullName(source.getFullName());
        target.setLocation(source.getLocation());
        target.setCurrentDesignation(source.getCurrentDesignation());
        target.setActive(source.getActive());
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

    }

    private void populateCandidateProfile(Candidate source, CandidateProfileDto target){

        target.setCvId(source.getCvId());
        target.setCvOwner(source.getCvOwner().getFullName());
        target.setReferredBy(source.getReferredBy());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());
        target.setLocation(source.getLocation());
        target.setSkills(source.getSkills());
        target.setCreatedAt(source.getCreatedAt());

    }

}