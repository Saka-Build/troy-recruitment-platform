package com.troy.ats.service.impl;

import com.troy.ats.entity.Permission;
import com.troy.ats.entity.Role;
import com.troy.ats.entity.RolePermission;
import com.troy.ats.entity.UserRole;
import com.troy.ats.repository.UserRoleRepository;
import com.troy.ats.service.EmployeeAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service("employeeAuthorizationService")
@RequiredArgsConstructor
public class EmployeeAuthorizationServiceImpl implements EmployeeAuthorizationService {

    private final UserRoleRepository userRoleRepository;

    /**
     *
     * @param employeeId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Set<GrantedAuthority> getAuthorities(UUID employeeId) {

        Set<GrantedAuthority> authorities = new HashSet<>();

        var userRoles = userRoleRepository.findActiveRolesWithPermissions(employeeId);

        for (UserRole userRole : userRoles) {

            Role role = userRole.getRole();

            // ROLE_RECRUITER
            // ROLE_ADMIN
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));

            for (RolePermission rolePermission : role.getRolePermissions()) {

                Permission permission = rolePermission.getPermission();

                String authority = permission.getModule().name() + "_" + permission.getAction().name();

                // JOB_READ
                // JOB_WRITE
                // CANDIDATE_READ
                // INTERVIEW_WRITE
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return authorities;
    }
}
