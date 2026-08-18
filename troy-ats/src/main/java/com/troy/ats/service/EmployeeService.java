package com.troy.ats.service;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Employee;
import com.troy.ats.enums.UserRole;
import com.troy.ats.searchfilter.dto.EmployeeExportFilter;
import com.troy.ats.searchfilter.dto.EmployeeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {
    public List<Employee> getAllEmployees() ;
    public Employee getEmployeeById(UUID id) ;

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

    /**
     *
     * @param employeeId
     * @param request
     * @param photo
     * @return
     */
    EmployeeDto updateEmployee(UUID employeeId, EmployeeCreateRequest request, MultipartFile photo);

    /**
     *
     * @param id
     * @return
     */
    EmployeeDto getEmployeeDtoById(UUID id);

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    Page<EmployeeDto> getEmployees(EmployeeFilter filter, Pageable pageable);

    /**
     *
     * @return
     */
    public EmployeesFiltersDto getEmployeeFilters();

    /**
     *
     * @param filter
     * @return
     * @throws IOException
     */
    byte[] exportEmployees(EmployeeExportFilter filter) throws IOException;

}

