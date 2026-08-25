package com.troy.ats.populator;

import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.JobDto;
import com.troy.ats.dto.RoleModuleResponseDto;
import com.troy.ats.dto.RoleResponseDto;
import com.troy.ats.entity.*;
import com.troy.ats.enums.PermissionAction;
import com.troy.ats.enums.PermissionModule;
import com.troy.ats.service.impl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.troy.ats.util.CommonUtil.enumToStringFormat;


@Component
@RequiredArgsConstructor
public class RolePopulator {

    public void populate(Role source, RoleResponseDto target) {

        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setName(source.getName());
        populateModulePermissions(source, target);

    }

    private void populateModulePermissions(Role source, RoleResponseDto target){

        Map<PermissionModule, Set<PermissionAction>>
                groupedPermissions = source.getRolePermissions().stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.groupingBy(Permission::getModule,
                                Collectors.mapping(Permission::getAction, Collectors.toSet()
                                )
                        )
                );

        List<RoleModuleResponseDto> modules =
                groupedPermissions.entrySet().stream()
                        .map(entry ->
                                new RoleModuleResponseDto(entry.getKey(), entry.getValue())
                        )
                        .sorted(Comparator.comparing(module -> module.getModule().name())
                        ).toList();

        target.setModules(modules);

    }

}