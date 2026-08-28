package com.troy.ats.populator;

import com.troy.ats.dto.SubmissionStatus;
import com.troy.ats.dto.SubmissionStatusRequest;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.service.EndClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class SubmissionStatusPopulator {

    private final EndClientService endClientService;
    private final EndClientPopulator endClientPopulator;

    public void populate(Status status, SubStatus subStatus, SubmissionStatus target) {

        if(Objects.nonNull(status)){

            populateStatus(status, target);
        }else {
            populateSubStaus(subStatus, target);
        }

    }

    private void populateStatus(Status source, SubmissionStatus target){
       target.setId(source.getId());
       target.setName(source.getName());
       target.setColourHex(source.getColourHex());
    }

    private void populateSubStaus(SubStatus source, SubmissionStatus target){
        target.setId(source.getId());
        target.setName(source.getName());
        target.setColourHex(source.getColourHex());
        target.setStatusIdForSubStatus(source.getStatus().getId());
    }

}