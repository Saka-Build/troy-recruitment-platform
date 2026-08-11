package com.troy.ats.service.impl;

import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Employee;
import com.troy.ats.repository.CandidateRepository;
import com.troy.ats.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {
    public List<Employee> getAllEmployees() ;
    public Optional<Employee> getEmployeeById(UUID id) ;

    public Employee createEmployee(Employee employee);

    public Employee updateEmployee(UUID id, Employee employee) ;

    public void deleteEmployee(UUID id);

    public Optional<Employee> getEmployeeByEmail(String email);
}

