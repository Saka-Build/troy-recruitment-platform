package com.troy.ats.service.jwt.impl;

import com.troy.ats.entity.Employee;
import com.troy.ats.repository.EmployeeRepository;
import com.troy.ats.service.jwt.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    /** Get employee by email id
     * @param emailId
     * @return
     * */
    @Override
    public Employee findByEmailId(String emailId) {
        Optional<Employee> employee =  employeeRepository.findByOfficialEmail(emailId);
        return employee.isPresent() ? employee.get() : null;
    }

    /**
     * Get employee by id
     *
     * @param id
     * @return
     *
     */
    @Override
    public Employee findById(UUID id) {
        return null;
    }

    /**
     * update employee
     *
     * @param employee
     */
    @Override
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}
