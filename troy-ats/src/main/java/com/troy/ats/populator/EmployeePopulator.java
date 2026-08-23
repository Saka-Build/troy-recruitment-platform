package com.troy.ats.populator;

import com.troy.ats.dto.CountryDto;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.entity.Country;
import com.troy.ats.entity.Employee;
import com.troy.ats.service.SessionService;
import com.troy.ats.util.CommonUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.troy.ats.util.CommonUtil.convertInstantToLocalDate;

@Component
public class EmployeePopulator {

    @Autowired
    @Resource(name="sessionService")
    private SessionService sessionService;

    public void populate(Employee source, EmployeeDto target) {

        target.setId(source.getId());
        target.setEmployeeCode(source.getEmployeeCode());
        target.setFullName(source.getFullName());
        target.setDesignation(source.getDesignation());
        target.setOfficialEmail(source.getOfficialEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());
        target.setRole(source.getRole());
        target.setPhotoUrl(source.getPhotoUrl());
        target.setLastLoginAt(convertInstantToLocalDate(source.getLastLoginAt(), sessionService));
        target.setActive(source.getIsActive());
        populateCountry(source, target);

    }

    private void populateCountry(Employee source, EmployeeDto target){

        CountryDto countryDto = new CountryDto();
        Country country = source.getCountry();
        countryDto.setCode(country.getCode());
        countryDto.setName(country.getName());

        target.setCountry(countryDto);
    }

}
