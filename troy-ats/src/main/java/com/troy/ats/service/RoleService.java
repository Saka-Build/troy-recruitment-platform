package com.troy.ats.service;

import com.troy.ats.dto.RoleCreateRequest;
import com.troy.ats.dto.RoleResponseDto;
import com.troy.ats.dto.RolesModulesListDto;
import com.troy.ats.entity.Permission;
import com.troy.ats.entity.Role;
import com.troy.ats.enums.PermissionAction;
import com.troy.ats.enums.PermissionModule;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    /**
     *
     * @param request
     * @return
     */
    RoleResponseDto createRole(RoleCreateRequest request);

    /**
     *
     * @param roleId
     * @param request
     * @return
     */
    RoleResponseDto updateRole(UUID roleId, RoleCreateRequest request);

    /**
     *
     * @param roleId
     */
    void deleteRole(UUID roleId);

    /**
     *
     * @param id
     * @return
     */
    Role getRoleById(UUID id);
    /**
     *
     * @param id
     * @return
     */
    RoleResponseDto getRoleDtoById(UUID id);

    /**
     *
     * @return
     */
    List<RoleResponseDto> getAllRoles();

    /**
     *
     * @param module
     * @param action
     * @return
     */
    Permission findPermissionByModuleAndAction(PermissionModule module, PermissionAction action);

    /**
     *
     * @param employeeId
     * @param roleId
     */
    void assignRole(UUID employeeId, UUID roleId);

    /**
     *
     * @return
     */
    List<RoleResponseDto> getRolesForEmployeeId(UUID employeeId);

    /**
     *
     * @param employeeId
     * @param roleId
     */
    void removeRoleForEmployee(UUID employeeId, UUID roleId);

    /**
     *
     * @return
     */
    RolesModulesListDto getRolesModules();
}