package com.troy.ats.repository;

import com.troy.ats.entity.Employee;
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

}

