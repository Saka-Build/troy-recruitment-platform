package com.troy.ats.service;
import com.troy.ats.entity.Employee;
import com.troy.ats.repository.CandidateRepository;
import com.troy.ats.repository.EmployeeRepository;
import com.troy.ats.service.impl.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {


    private  EmployeeRepository employeeRepository;
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(UUID id, Employee employee) {
        // candidate.setId(id);
        return employeeRepository.save(employee);
    }
    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByOfficialEmail(email);
    }
}
