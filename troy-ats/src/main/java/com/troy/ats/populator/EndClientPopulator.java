package com.troy.ats.populator;

import com.troy.ats.dto.EndClientCreateRequest;
import com.troy.ats.dto.EndClientDto;
import com.troy.ats.entity.EndClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class EndClientPopulator {

    public void populate(EndClient source, EndClientDto target) {

        target.setId(source.getId());
        target.setName(source.getName());
        target.setActive(source.getActive());
    }


}