package com.troy.ats.populator;

import com.troy.ats.dto.EmployeeCreateRequest;
import com.troy.ats.entity.Employee;
import com.troy.ats.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReverseEmployeePopulator {

    private final PasswordEncoder passwordEncoder;

    public void populate(EmployeeCreateRequest source, Employee target) {

        target.setEmployeeCode(source.getEmployeeCode());
        target.setFullName(source.getFullName());
        target.setDesignation(source.getDesignation());
        target.setOfficialEmail(source.getOfficialEmail());
        target.setPersonalEmail(source.getPersonalEmail());
        target.setPhone(source.getPhone());
        target.setWhatsapp(source.getWhatsapp());
        target.setRole(UserRole.fromValue(source.getRole()));
        target.setPasswordHash(passwordEncoder.encode(source.getPassword()));
        //target.setIsActive(source.getIsActive() != null ? source.getIsActive() : true);
        target.setCountryCode(source.getCountryCode());
        target.setFailedLoginAttempts(0);

    }

}