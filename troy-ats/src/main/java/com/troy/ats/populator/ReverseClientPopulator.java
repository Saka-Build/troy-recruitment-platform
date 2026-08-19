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

        if (source.getName() != null) {
            target.setName(source.getName());
        }

        if (source.getContactPerson() != null) {
            target.setContactPerson(source.getContactPerson());
        }

        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }

        if (source.getPhone() != null) {
            target.setPhone(source.getPhone());
        }

        if (source.getWhatsapp() != null) {
            target.setWhatsapp(source.getWhatsapp());
        }

        if (source.getIndustry() != null) {
            target.setIndustry(source.getIndustry());
        }

        if (source.getStatus() != null) {
            target.setStatus(source.getStatus());
        }

        if (source.getAddress() != null) {
            target.setAddress(source.getAddress());
        }

        if (source.getNotes() != null) {
            target.setNotes(source.getNotes());
        }

        if (source.getIsActive() != null) {
            target.setIsActive(source.getIsActive());
        }

        // country
        if (source.getCountryCode() != null) {
            Country country = countryService.getCountryByCode(source.getCountryCode());
            target.setCountry(country);

        }

    }


}