package com.troy.ats.repository;

import com.troy.ats.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByOfficialEmail(String email);
    Optional<Employee> findByEmployeeCode(String employeeCode);
}

