package com.troy.ats.repository;

import com.troy.ats.entity.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository
        extends JpaRepository<UserRole, UUID> {

    @Query("""
        SELECT DISTINCT ur
        FROM UserRole ur
        JOIN FETCH ur.role r
        LEFT JOIN FETCH r.rolePermissions rp
        LEFT JOIN FETCH rp.permission p
        WHERE ur.user.id = :employeeId
        AND ur.active = true
    """)
    List<UserRole> findActiveRolesWithPermissions(
            @Param("employeeId") UUID employeeId
    );

    boolean existsByUserIdAndRoleIdAndActiveTrue(
            UUID userId,
            UUID roleId
    );

    List<UserRole> findByUserIdAndActiveTrue(UUID userId);

    @EntityGraph(attributePaths = {"role"})
    Optional<UserRole> findByUserIdAndRoleId(
            UUID userId,
            UUID roleId
    );

    void deleteByRoleId(UUID roleId);

    boolean existsByRoleId(UUID roleId);
}