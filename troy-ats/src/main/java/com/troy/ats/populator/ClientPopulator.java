package com.troy.ats.populator;

import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.EndClientDto;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.EndClient;
import com.troy.ats.service.EndClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.troy.ats.util.CommonUtil.enumToStringFormat;


@Component
@RequiredArgsConstructor
public class ClientPopulator {

    private final EndClientService endClientService;
    private final EndClientPopulator endClientPopulator;

    public void populate(Client source, ClientDto target) {

        target.setId(source.getId());
        target.setName(source.getName());
        target.setContactPerson(source.getContactPerson());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());
        target.setIndustry(source.getIndustry());

        String result = source.getStatus() == null || source.getStatus().isEmpty() ? source.getStatus()
                : enumToStringFormat( source.getStatus());
        target.setStatus(result);

        target.setAddress(source.getAddress());
        target.setNotes(source.getNotes());
        target.setIsActive(source.getIsActive());
        target.setSource(source.getSource());

        // Country
        if (source.getCountry() != null) {
            target.setCountryId(source.getCountry().getId());
            target.setCountryCode(source.getCountry().getCode());
            target.setCountryName(source.getCountry().getName());
        }

        populateEndClients(source, target);

    }

    private void  populateEndClients(Client source, ClientDto target){

        if (source.getEndClientIds() == null || source.getEndClientIds().length == 0) {

            target.setEndClients(Collections.emptyList());
            return;
        }
        List<EndClient> endClients = endClientService.getEndClients( Arrays.asList(source.getEndClientIds()));

        List<EndClientDto> endClientDtos =  endClients.stream().map(endClient -> {
            EndClientDto endClientDto = new EndClientDto();
            endClientPopulator.populate(endClient, endClientDto);
            return endClientDto;
        }).toList();

        target.setEndClients(endClientDtos);
    }

}