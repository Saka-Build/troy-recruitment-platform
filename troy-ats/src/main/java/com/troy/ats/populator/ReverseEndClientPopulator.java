package com.troy.ats.populator;

import com.troy.ats.dto.EndClientCreateRequest;
import com.troy.ats.entity.EndClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ReverseEndClientPopulator {

    public void populate(EndClientCreateRequest source, EndClient target) {

        if(Objects.nonNull(source.getName())){
            target.setName(source.getName());
        }
        if(Objects.nonNull(source.getActive())){
            target.setActive(source.getActive());
        }
    }


}