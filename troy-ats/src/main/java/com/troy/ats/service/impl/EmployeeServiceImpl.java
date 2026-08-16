package com.troy.ats.service.impl;

import com.troy.ats.dto.EmployeeCreateRequest;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.EmployeesFiltersDto;
import com.troy.ats.entity.Employee;
import com.troy.ats.populator.EmployeePopulator;
import com.troy.ats.populator.ReverseEmployeePopulator;
import com.troy.ats.repository.EmployeeRepository;
import com.troy.ats.searchfilter.dto.EmployeeFilter;
import com.troy.ats.searchfilter.filter.EmployeeSpecification;
import com.troy.ats.service.EmployeeService;
import com.troy.ats.service.FileStorageService;
import com.troy.ats.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("employeeService")
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmployeePopulator employeePopulator;
    private final ReverseEmployeePopulator reverseEmployeePopulator;
    private final FileStorageService fileStorageService;


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

    /**
     *
     * @param request
     * @param photo
     * @return
     */
    @Override
    @Transactional
    public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile photo) {
        // Employee code
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {

            throw new IllegalArgumentException("Employee code already exists");
        }

        // Official email
        if (employeeRepository.existsByOfficialEmailIgnoreCase(request.getOfficialEmail())) {

            throw new IllegalArgumentException("Official email already exists");
        }

        // Personal email
        if (request.getPersonalEmail() != null && !request.getPersonalEmail().isBlank()
                && employeeRepository.existsByPersonalEmailIgnoreCase(request.getPersonalEmail())) {

            throw new IllegalArgumentException("Personal email already exists");
        }

        // Create employee
        Employee employee = new Employee();
        reverseEmployeePopulator.populate(request, employee);

        // Save employee first
        employee = employeeRepository.save(employee);

        // Upload photo
        if (photo != null && !photo.isEmpty()) {

            CommonUtil.validatePhoto(photo);
            String photoUrl = fileStorageService.store(photo, employee.getId(),Boolean.FALSE, Boolean.TRUE);
            employee.setPhotoUrl(photoUrl);

            employee = employeeRepository.save(employee);
        }
        EmployeeDto employeeDto = new EmployeeDto();
        employeePopulator.populate(employee, employeeDto);

        return employeeDto;
    }

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Override
    public Page<EmployeeDto> getEmployees(EmployeeFilter filter, Pageable pageable) {
        return employeeRepository.findAll(EmployeeSpecification.filter(filter), pageable)
                .map(employee -> {
                    EmployeeDto dto = new EmployeeDto();
                    employeePopulator.populate(employee, dto);
                    return dto;
                });
    }

    /**
     *
     * @return
     */
    @Override
    public EmployeesFiltersDto getEmployeeFilters() {

        long totalEmployees = employeeRepository.count();
        long totalActiveEmployees = employeeRepository.countByIsActive(Boolean.TRUE);

        EmployeesFiltersDto employeesFiltersDto = new EmployeesFiltersDto();
        employeesFiltersDto.setTotalEmployees(totalEmployees);
        employeesFiltersDto.setTotalActiveEmployees(totalActiveEmployees);

        return employeesFiltersDto;

    }

}
