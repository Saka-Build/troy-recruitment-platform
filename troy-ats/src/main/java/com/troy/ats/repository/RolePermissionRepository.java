package com.troy.ats.repository;

import com.troy.ats.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RolePermissionRepository
        extends JpaRepository<RolePermission, UUID> {

    void deleteByRoleId(UUID roleId);
}