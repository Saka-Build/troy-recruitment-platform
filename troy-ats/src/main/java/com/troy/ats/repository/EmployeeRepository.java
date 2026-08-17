package com.troy.ats.repository;

import com.troy.ats.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {
    Optional<Employee> findByOfficialEmail(String officialEmail);
    boolean existsByEmployeeCode(String employeeCode);
    boolean existsByOfficialEmailIgnoreCase(String officialEmail);
    boolean existsByPersonalEmailIgnoreCase(String personalEmail);
    Optional<Employee> findByOfficialEmailIgnoreCase(String officialEmail);
    long countByIsActive(boolean isActive);

    @Override
    @EntityGraph(attributePaths = {"country"})
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"country"})
    Optional<Employee> findById(UUID id);

}

