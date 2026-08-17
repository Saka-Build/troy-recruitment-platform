package com.troy.ats.populator;

import com.troy.ats.dto.EmployeeCreateRequest;
import com.troy.ats.entity.Employee;
import com.troy.ats.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ReverseEmployeePopulator {

    private final PasswordEncoder passwordEncoder;

    public void populate(EmployeeCreateRequest source, Employee target) {

        if(Objects.nonNull(source.getEmployeeCode())){
            target.setEmployeeCode(source.getEmployeeCode());
        }
        if(Objects.nonNull(source.getFullName())){
            target.setFullName(source.getFullName());
        }
        if(Objects.nonNull(source.getDesignation())){
            target.setDesignation(source.getDesignation());
        }
        if(Objects.nonNull(source.getOfficialEmail())){
            target.setOfficialEmail(source.getOfficialEmail());
        }
        if(Objects.nonNull(source.getPersonalEmail())){
            target.setPersonalEmail(source.getPersonalEmail());
        }
        if(Objects.nonNull(source.getPhone())){
            target.setPhone(source.getPhone());
        }
        if(Objects.nonNull(source.getWhatsapp())){
            target.setWhatsapp(source.getWhatsapp());
        }
        if(Objects.nonNull(source.getRole())){
            target.setRole(UserRole.fromValue(source.getRole()));
        }
        if(Objects.nonNull(source.getPassword())){
            target.setPasswordHash(passwordEncoder.encode(source.getPassword()));
        }
        if(Objects.nonNull(source.getCountryCode())){
            target.setCountryCode(source.getCountryCode());
        }
        if(Objects.nonNull(source.getActive())){
            target.setIsActive(source.getActive());
        }

    }

}