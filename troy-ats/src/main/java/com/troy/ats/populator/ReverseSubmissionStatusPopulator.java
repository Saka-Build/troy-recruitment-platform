package com.troy.ats.populator;

import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.EndClientDto;
import com.troy.ats.dto.SubmissionStatusRequest;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.EndClient;
import com.troy.ats.entity.Status;
import com.troy.ats.entity.SubStatus;
import com.troy.ats.service.EndClientService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ReverseSubmissionStatusPopulator {

    private final EndClientService endClientService;
    private final EndClientPopulator endClientPopulator;

    public void populate(SubmissionStatusRequest source, Status status, SubStatus subStatus) {

        if(Objects.nonNull(status)){

            populateStatus(source, status);
        }else {
            populateSubStaus(source, subStatus);
        }

    }

    private void populateStatus(SubmissionStatusRequest source, Status target){
        if(Objects.nonNull(source.getName())){
            target.setName(source.getName());
        }
        if(Objects.nonNull(source.getColourHex())){
            target.setColourHex(source.getColourHex());
        }
        if(Objects.nonNull(source.getActive())){
            target.setActive(source.getActive());
        }
        if(Objects.nonNull(source.getSortOrder())){
            target.setSortOrder(Short.valueOf(source.getSortOrder()));
        }
    }

    private void populateSubStaus(SubmissionStatusRequest source, SubStatus target){
        if(Objects.nonNull(source.getName())){
            target.setName(source.getName());
        }
        if(Objects.nonNull(source.getColourHex())){
            target.setColourHex(source.getColourHex());
        }
        if(Objects.nonNull(source.getActive())){
            target.setActive(source.getActive());
        }
        if(Objects.nonNull(source.getSortOrder())){
            target.setSortOrder(Short.valueOf(source.getSortOrder()));
        }
    }

}