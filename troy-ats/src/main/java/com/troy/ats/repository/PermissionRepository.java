package com.troy.ats.repository;

import com.troy.ats.entity.Permission;
import com.troy.ats.enums.PermissionAction;
import com.troy.ats.enums.PermissionModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository
        extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByModuleAndAction(PermissionModule module, PermissionAction action);

    boolean existsByModuleAndAction(PermissionModule module, PermissionAction action);
}