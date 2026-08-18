package com.troy.ats.populator;

import com.troy.ats.dto.ClientDto;
import com.troy.ats.entity.Client;
import org.springframework.stereotype.Component;


@Component
public class ClientPopulator {


    public void populate(Client source, ClientDto target) {

        target.setId(source.getId());
        target.setName(source.getName());
        target.setContactPerson(source.getContactPerson());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());

        target.setIndustry(source.getIndustry());
        target.setStatus(source.getStatus());
        target.setAddress(source.getAddress());
        target.setNotes(source.getNotes());
        target.setIsActive(source.getIsActive());

        // Country
        if (source.getCountry() != null) {
            target.setCountryId(source.getCountry().getId());
            target.setCountryCode(source.getCountry().getCode());
            target.setCountryName(source.getCountry().getName());
        }

    }

}