package com.troy.ats.service;

import com.troy.ats.dto.EmployeeCreateRequest;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     *
     * @param employee
     * @return
     */
    public EmployeeDto getEmployeeDtoFromEntity(Employee employee);

    /**
     *
     * @param request
     * @param photo
     * @return
     */
    EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile photo);
}

