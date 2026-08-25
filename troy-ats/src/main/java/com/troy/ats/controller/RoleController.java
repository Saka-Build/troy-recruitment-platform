package com.troy.ats.controller;


import com.troy.ats.dto.AssignRoleRequest;
import com.troy.ats.dto.RoleCreateRequest;
import com.troy.ats.dto.RoleResponseDto;
import com.troy.ats.dto.RolesModulesListDto;
import com.troy.ats.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    //@PreAuthorize("hasAuthority('ROLE_WRITE')")
    public ResponseEntity<RoleResponseDto> createRole(
                                            @Valid @RequestBody RoleCreateRequest request) {

        RoleResponseDto response = roleService.createRole(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(@PathVariable UUID id,
                                                    @RequestBody RoleCreateRequest request) {

        RoleResponseDto response = roleService.updateRole(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable UUID roleId) {

        roleService.deleteRole(roleId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable UUID id) {

        return ResponseEntity.ok(roleService.getRoleDtoById(id));
    }

    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<List<RoleResponseDto>> getRoles() {

        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/employee/{employeeId}")
    public ResponseEntity<Void> assignRole(
                                        @PathVariable UUID employeeId,
                                        @RequestBody @Valid AssignRoleRequest request) {

        roleService.assignRole(employeeId, request.getRoleId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<RoleResponseDto>> getRoles(
                                                @PathVariable UUID employeeId) {

        return ResponseEntity.ok(roleService.getRolesForEmployeeId(employeeId));
    }

    @DeleteMapping("/{roleId}/employee/{employeeId}")
    public ResponseEntity<Void> removeRole(
            @PathVariable UUID employeeId,
            @PathVariable UUID roleId) {

        roleService.removeRoleForEmployee(employeeId, roleId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allRolesAndModules")
    //@PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<RolesModulesListDto> getRolesModules() {

        return ResponseEntity.ok(roleService.getRolesModules());
    }

}