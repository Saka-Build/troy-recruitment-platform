package com.troy.ats.service.impl;

import com.troy.ats.dto.CandidateDto;
import com.troy.ats.dto.EmployeeCreateRequest;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.EmployeesFiltersDto;
import com.troy.ats.entity.Candidate;
import com.troy.ats.entity.Employee;
import com.troy.ats.exception.DuplicateResourceException;
import com.troy.ats.populator.EmployeePopulator;
import com.troy.ats.populator.ReverseEmployeePopulator;
import com.troy.ats.repository.EmployeeRepository;
import com.troy.ats.searchfilter.dto.EmployeeExportFilter;
import com.troy.ats.searchfilter.dto.EmployeeFilter;
import com.troy.ats.searchfilter.filter.EmployeeSpecification;
import com.troy.ats.service.EmployeeService;
import com.troy.ats.service.FileStorageService;
import com.troy.ats.util.CommonUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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


    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
    }



    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(UUID id, Employee employee) {
        // candidate.setId(id);
        return employeeRepository.save(employee);
    }

    @Override
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

        validateEmployee(request);

        // Create employee
        Employee employee = new Employee();
        reverseEmployeePopulator.populate(request, employee);
        employee.setIsActive(Boolean.TRUE);
        employee.setFailedLoginAttempts(0);

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

    @Override
    @Transactional
    public EmployeeDto updateEmployee(UUID employeeId, EmployeeCreateRequest request, MultipartFile photo) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));

        reverseEmployeePopulator.populate(request, employee);
        // Save employee first
        employee = employeeRepository.save(employee);

        // Upload photo
        if (photo != null && !photo.isEmpty()) {

            CommonUtil.validatePhoto(photo);
            // Keep old URL before replacing
            String oldPhotoUrl = employee.getPhotoUrl();
            // Delete old CV
            if (oldPhotoUrl != null && !oldPhotoUrl.isBlank()) {

                fileStorageService.delete(oldPhotoUrl, Boolean.FALSE, Boolean.TRUE);
            }
            String photoUrl = fileStorageService.store(photo, employee.getId(), Boolean.FALSE, Boolean.TRUE);
            employee.setPhotoUrl(photoUrl);

            employee = employeeRepository.save(employee);
        }
        EmployeeDto employeeDto = new EmployeeDto();
        employeePopulator.populate(employee, employeeDto);

        return employeeDto;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public EmployeeDto getEmployeeDtoById(UUID id) {

        EmployeeDto employeeDto = employeeRepository.findById(id)
                .map(employee -> {
                    EmployeeDto dto = new EmployeeDto();
                    employeePopulator.populate(employee, dto);
                    return dto;
                }) .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
        return employeeDto;
    }

    private void validateEmployee(EmployeeCreateRequest request){

        // Employee code
        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {

            throw new DuplicateResourceException("Employee code already exists");
        }

        // Official email
        if (employeeRepository.existsByOfficialEmailIgnoreCase(request.getOfficialEmail())) {

            throw new DuplicateResourceException("Official email already exists");
        }

        // Personal email
        if (request.getPersonalEmail() != null && !request.getPersonalEmail().isBlank()
                && employeeRepository.existsByPersonalEmailIgnoreCase(request.getPersonalEmail())) {

            throw new DuplicateResourceException("Personal email already exists");
        }
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

    @Override
    @Transactional(readOnly = true)
    public byte[] exportEmployees(EmployeeExportFilter filter) throws IOException {

        Specification<Employee> specification = EmployeeSpecification.exportFilter(filter);

        List<Employee> employees = employeeRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "createdAt"));

        return createExcel(employees);
    }

    /**
     *
     * @param employeesId
     * @return
     */
    @Override
    public List<Employee> getEmployeesByIds(List<UUID> employeesIds) {

        if(CollectionUtils.isNotEmpty(employeesIds)){
            return employeeRepository.findAllByIdIn(employeesIds);
        }
        return Collections.emptyList();
    }

    private byte[] createExcel(List<Employee> employees) throws IOException {

        try (Workbook workbook = new HSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Employees");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            headerStyle.setFont(headerFont);

            // Header
            Row header = sheet.createRow(0);

            String[] columns = {
                    "Employee Code",
                    "Full Name",
                    "Designation",
                    "Official Email",
                    "Personal Email",
                    "Phone",
                    "WhatsApp",
                    "Role",
                    "Active",
                    "Country Code",
                    "Created At",
                    "Updated At",
                    "Last Login At"
            };

            for (int i = 0; i < columns.length; i++) {

                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;

            for (Employee employee : employees) {

                Row row = sheet.createRow(rowNum++);

                int col = 0;

                row.createCell(col++).setCellValue(value(employee.getEmployeeCode()));
                row.createCell(col++).setCellValue(value(employee.getFullName()));
                row.createCell(col++).setCellValue(value(employee.getDesignation()));
                row.createCell(col++).setCellValue(value(employee.getOfficialEmail()));
                row.createCell(col++).setCellValue(value(employee.getPersonalEmail()));
                row.createCell(col++).setCellValue(value(employee.getPhone()));
                row.createCell(col++).setCellValue(value(employee.getWhatsapp()));
                row.createCell(col++).setCellValue(employee.getRole() != null ? employee.getRole().name() : "");
                row.createCell(col++).setCellValue(employee.getIsActive() != null ? employee.getIsActive() : false);
                row.createCell(col++).setCellValue(value(employee.getCountry().getCode()));
                row.createCell(col++).setCellValue(employee.getCreatedAt() != null ? employee.getCreatedAt().toString() : "");
                row.createCell(col++).setCellValue(employee.getUpdatedAt() != null ? employee.getUpdatedAt().toString() : "");
                row.createCell(col++).setCellValue(employee.getLastLoginAt() != null ? employee.getLastLoginAt().toString() : "");
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();
        }
    }

    private String value(String value) {
        return value != null ? value : "";
    }

}
