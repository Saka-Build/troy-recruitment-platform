package com.troy.ats.service.impl;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.entity.Employee;
import com.troy.ats.populator.EmployeePopulator;
import com.troy.ats.repository.EmployeeRepository;
import com.troy.ats.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {


    private  EmployeeRepository employeeRepository;
    private EmployeePopulator employeePopulator;
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

    /**
     *
     * @param employee
     * @return
     */
    @Override
    public EmployeeDto getEmployeeDtoFromEntity(Employee employee) {

        EmployeeDto employeeDto = new EmployeeDto();
        employeePopulator.populate(employee, employeeDto);
        return employeeDto;
    }
}
