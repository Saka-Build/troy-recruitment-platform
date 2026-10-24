package com.troy.ats.service.impl;

import com.troy.ats.entity.Permission;
import com.troy.ats.entity.Role;
import com.troy.ats.entity.RolePermission;
import com.troy.ats.entity.UserRole;
import com.troy.ats.repository.UserRoleRepository;
import com.troy.ats.service.EmployeeAuthorizationService;
import com.troy.ats.service.RoleService;
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
    private final RoleService roleService;

    /**
     *
     * @param employeeId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Set<GrantedAuthority> getAuthorities(UUID employeeId, UUID roleId) {

        Set<GrantedAuthority> authorities = new HashSet<>();

        UserRole userRole = roleService.findByUserIdAndRoleId(employeeId, roleId);

        Role role = userRole.getRole();

        // ROLE_ADMIN
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));

        for (RolePermission rolePermission : role.getRolePermissions()) {

            Permission permission = rolePermission.getPermission();

            String authority = permission.getModule().name() + "_" + permission.getAction().name();

            authorities.add(new SimpleGrantedAuthority(authority));
        }

        return authorities;
    }
}
