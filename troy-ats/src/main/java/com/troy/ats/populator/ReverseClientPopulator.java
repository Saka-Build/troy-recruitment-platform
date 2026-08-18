package com.troy.ats.populator;

import com.troy.ats.dto.ClientCreateRequest;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.Country;
import com.troy.ats.service.impl.CountryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReverseClientPopulator {

    private final CountryServiceImpl countryService;

    public void populate(ClientCreateRequest source, Client target) {

        target.setName(source.getName());
        target.setContactPerson(source.getContactPerson());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());
        target.setIndustry(source.getIndustry());
        target.setStatus(source.getStatus());
        target.setAddress(source.getAddress());
        target.setNotes(source.getNotes());

        target.setIsActive(source.getIsActive() != null ? source.getIsActive() : true);

        // country
        if (source.getCountryCode() != null) {
            Country country = countryService.getCountryByCode(source.getCountryCode());
            target.setCountry(country);

        }

    }


}