package com.troy.ats.service.impl;

import com.troy.ats.dto.RoleCreateRequest;
import com.troy.ats.dto.RoleResponseDto;
import com.troy.ats.dto.RolesModulesListDto;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Permission;
import com.troy.ats.entity.Role;
import com.troy.ats.entity.UserRole;
import com.troy.ats.enums.PermissionAction;
import com.troy.ats.enums.PermissionModule;
import com.troy.ats.enums.RoleName;
import com.troy.ats.populator.ReverseRolePopulator;
import com.troy.ats.populator.RolePopulator;
import com.troy.ats.repository.PermissionRepository;
import com.troy.ats.repository.RolePermissionRepository;
import com.troy.ats.repository.RoleRepository;
import com.troy.ats.repository.UserRoleRepository;
import com.troy.ats.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.troy.ats.util.CommonUtil.enumToStringFormat;

@Service("roleService")
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ReverseRolePopulator reverseRolePopulator;
    private final RolePopulator rolePopulator;
    private final EmployeeServiceImpl employeeService;
    private final UserRoleRepository userRoleRepository;
    private final SessionServiceImpl sessionService;
    private final RolePermissionRepository rolePermissionRepository;


    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public RoleResponseDto createRole(RoleCreateRequest request) {

        String roleName = request.getRoleName().trim().toUpperCase(Locale.ROOT);

        if (roleRepository.existsByName(RoleName.valueOf(roleName))) {
            throw new IllegalArgumentException("Role already exists: " + roleName);
        }
        Set<Permission> permissions = resolvePermissions(request);
        Role role = new Role();
        reverseRolePopulator.populate(request, role, permissions);

        Role savedRole = roleRepository.save(role);

        RoleResponseDto roleResponseDto = new RoleResponseDto();
        rolePopulator.populate(role, roleResponseDto);
        return roleResponseDto;
    }

    /**
     *
     * @param roleId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public RoleResponseDto updateRole(UUID roleId, RoleCreateRequest request) {

        Role role = getRoleById(roleId);
        Set<Permission> permissions = resolvePermissions(request);

        // Add new permissions
        reverseRolePopulator.populate(request, role, permissions);

        //Role savedRole = roleRepository.save(role);

        RoleResponseDto response = new RoleResponseDto();
        rolePopulator.populate(role, response);

        return response;
    }

    /**
     *
     * @param roleId
     */
    @Override
    @Transactional
    public void deleteRole(UUID roleId) {

        if (!roleRepository.existsById(roleId)) {
            throw new EntityNotFoundException("Role not found: " + roleId);
        }

        // Remove employee -> role assignments
        userRoleRepository.deleteByRoleId(roleId);

        // Remove role -> permission assignments
        rolePermissionRepository.deleteByRoleId(roleId);

        // Finally delete the role
        roleRepository.deleteById(roleId);
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Role getRoleById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + id));

        return role;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto getRoleDtoById(UUID id) {

        Role role = getRoleById(id);
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        rolePopulator.populate(role, roleResponseDto);
        return roleResponseDto;
    }

    /**
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(role -> {
                    RoleResponseDto response = new RoleResponseDto();
                    rolePopulator.populate(role, response);
                    return response;
                })
                .toList();
    }

    /**
     *
     * @param module
     * @param action
     * @return
     */
    @Override
    public Permission findPermissionByModuleAndAction(PermissionModule module, PermissionAction action) {

        Permission permission = permissionRepository.findByModuleAndAction(module, action)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found: " + module + "_" + action));

        return permission;
    }

    /**
     *
     * @param employeeId
     * @param roleId
     */
    @Override
    @Transactional
    public void assignRole(UUID employeeId, UUID roleId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        Role role = getRoleById(roleId);
        if (userRoleRepository.existsByUserIdAndRoleIdAndActiveTrue(employeeId, roleId)) {

            throw new IllegalArgumentException("Role is already assigned to employee");
        }
        UserRole userRole = UserRole.builder()
                .user(employee)
                .role(role)
                .assignedBy(sessionService.getCurrentUser())
                .active(true)
                .build();

        userRoleRepository.save(userRole);
    }

    /**
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> getRolesForEmployeeId(UUID employeeId) {
        if (!employeeService.employeeExistsById(employeeId)) {
            throw new EntityNotFoundException(
                    "Employee not found: " + employeeId
            );
        }

        return userRoleRepository
                .findByUserIdAndActiveTrue(employeeId)
                .stream()
                .map(UserRole::getRole)
                .map(role -> {
                    RoleResponseDto response = new RoleResponseDto();
                    rolePopulator.populate(role, response);
                    return response;
                })
                .toList();
    }

    /**
     *
     * @param employeeId
     * @param roleId
     */
    @Override
    public void removeRoleForEmployee(UUID employeeId, UUID roleId) {

        if (!employeeService.employeeExistsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found: " + employeeId);
        }

        if (!roleRepository.existsById(roleId)) {
            throw new EntityNotFoundException("Role not found: " + roleId);
        }

        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(employeeId, roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role is not assigned to employee"));

        userRoleRepository.delete(userRole);
    }

    /**
     *
     * @return
     */
    @Override
    public RolesModulesListDto getRolesModules() {

        List<String> roles = List.of(enumToStringFormat(RoleName.SUPER_ADMIN.name()),
                                        enumToStringFormat(RoleName.ADMIN.name()),
                                        enumToStringFormat(RoleName.LEAD_RECRUITER.name()),
                                        enumToStringFormat(RoleName.RECRUITER.name())
                                    );

        List<String> modules = List.of(enumToStringFormat(PermissionModule.ROLE.name()),
                enumToStringFormat(PermissionModule.USER.name()),
                enumToStringFormat(PermissionModule.JOB.name()),
                enumToStringFormat(PermissionModule.CLIENT.name()),
                enumToStringFormat(PermissionModule.CANDIDATE.name()),
                enumToStringFormat(PermissionModule.SUBMISSION.name()),
                enumToStringFormat(PermissionModule.PERMISSION.name()),
                enumToStringFormat(PermissionModule.INTERVIEW.name())
        );

        List<String> permissions = List.of(enumToStringFormat(PermissionAction.WRITE.name()),
                enumToStringFormat(PermissionAction.READ.name()),
                enumToStringFormat(PermissionAction.DELETE.name())
        );

        RolesModulesListDto rolesModulesListDto = new RolesModulesListDto();
        rolesModulesListDto.setRoles(roles);
        rolesModulesListDto.setModules(modules);
        rolesModulesListDto.setPermissions(permissions);

        return rolesModulesListDto;
    }

    private Set<Permission> resolvePermissions(RoleCreateRequest request) {

        Set<Permission> permissions = new HashSet<>();

        if (request.getPermissions() == null) {
            return permissions;
        }

        request.getPermissions().forEach((module, actions) -> {
            PermissionModule permissionModule = PermissionModule.fromValue(module);

            if (actions == null) {
                return;
            }

            for (String action : actions) {

                PermissionAction permissionAction = PermissionAction.fromValue(action);
                Permission permission =  getOrCreatePermission(permissionModule, permissionAction);
                permissions.add(permission);
            }
        });

        return permissions;
    }

    private Permission getOrCreatePermission(PermissionModule module, PermissionAction action) {

        return permissionRepository.findByModuleAndAction(module, action)
                .orElseGet(() -> {

                    Permission permission = new Permission();

                    permission.setModule(module);
                    permission.setAction(action);
                    permission.setDescription(module.name() + " " + action.name());

                    return permissionRepository.save(permission);
                });
    }
}