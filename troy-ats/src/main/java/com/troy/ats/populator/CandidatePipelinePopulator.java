package com.troy.ats.populator;

import com.troy.ats.dto.CandidatePipelineDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Submission;
import org.springframework.stereotype.Component;


@Component
public class CandidatePipelinePopulator {


    public void populate(Submission source, CandidatePipelineDto target) {

        Candidate candidate = source.getCandidate();
        target.setId(candidate.getId());
        target.setFullName(candidate.getFullName());
        target.setEmail(candidate.getEmail());
        target.setCurrentDesignation(candidate.getCurrentDesignation());
        target.setSource(candidate.getSource());
        target.setJobTitle(source.getJob().getTitle());

    }

}
