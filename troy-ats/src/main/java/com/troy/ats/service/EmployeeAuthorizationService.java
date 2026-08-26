package com.troy.ats.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

public interface EmployeeAuthorizationService {

    /**
     *
     * @param employeeId
     * @return
     */
    Set<GrantedAuthority> getAuthorities(UUID employeeId, UUID roleId);

}

