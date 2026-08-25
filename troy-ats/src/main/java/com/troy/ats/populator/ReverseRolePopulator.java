package com.troy.ats.populator;

import com.troy.ats.dto.RoleCreateRequest;
import com.troy.ats.entity.Permission;
import com.troy.ats.entity.Role;
import com.troy.ats.entity.RolePermission;
import com.troy.ats.enums.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ReverseRolePopulator {

    public void populate(RoleCreateRequest source, Role target, Set<Permission> permissions) {

       if(Objects.nonNull(source.getRoleName())){
           target.setName(RoleName.fromValue(source.getRoleName()));
       }
       if(Objects.nonNull(source.getPermissions())){
            populatePermissions(source, target,permissions);
       }

    }

    private void populatePermissions(RoleCreateRequest source, Role target, Set<Permission> requestedPermissions){
        Set<RolePermission> existing = target.getRolePermissions();

        // Remove permissions that are no longer requested
        existing.removeIf(rp ->
                !requestedPermissions.contains(rp.getPermission())
        );

        // Add only permissions that don't already exist
        Set<Permission> existingPermissions = existing.stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toSet());

        for (Permission permission : requestedPermissions) {

            if (!existingPermissions.contains(permission)) {

                RolePermission rolePermission = new RolePermission();
                rolePermission.setRole(target);
                rolePermission.setPermission(permission);

                existing.add(rolePermission);
            }
        }

    }

}