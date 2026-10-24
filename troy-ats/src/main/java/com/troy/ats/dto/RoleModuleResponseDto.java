package com.troy.ats.dto;

import com.troy.ats.enums.PermissionAction;
import com.troy.ats.enums.PermissionModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleModuleResponseDto {

    private PermissionModule module;
    private Set<PermissionAction> permissions;

}
