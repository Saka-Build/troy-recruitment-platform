package com.troy.ats.repository;

import com.troy.ats.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository
        extends JpaRepository<Role, UUID> {

    boolean existsByNameIgnoreCase(String name);

}