package com.troy.ats.controller;

import com.troy.ats.dto.EmployeeCreateRequest;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.dto.EmployeesFiltersDto;
import com.troy.ats.entity.Employee;
import com.troy.ats.searchfilter.dto.EmployeeExportFilter;
import com.troy.ats.searchfilter.dto.EmployeeFilter;
import com.troy.ats.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(("/allEmployees"))
    @PreAuthorize("hasAuthority('USER_READ')")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<Page<EmployeeDto>> getEmployees(@RequestParam(required = false) String search,
                                                           @RequestParam(required = false) Boolean active,
                                                           @RequestParam(required = false) String designation,
                                                           @RequestParam(required = false) OffsetDateTime createdFrom,
                                                           @RequestParam(required = false) OffsetDateTime createdTo,
                                                           @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        EmployeeFilter filter = new EmployeeFilter(search, active, designation, createdFrom, createdTo);

        return ResponseEntity.ok(employeeService.getEmployees(filter, pageable));
    }

    @GetMapping("/employeefilters")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<EmployeesFiltersDto> getCandidateFilters() {

        return ResponseEntity.ok(employeeService.getEmployeeFilters());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable UUID id) {

        return ResponseEntity.ok(employeeService.getEmployeeDtoById(id));
    }

    @GetMapping("email/{email}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        return employeeService.getEmployeeByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<EmployeeDto> createEmployee(
            @RequestPart("employee") @Valid EmployeeCreateRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        EmployeeDto employeeDto = employeeService.createEmployee(request, photo);

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeDto);

    }

    @PutMapping(value = "/update/{employeeId}")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable UUID employeeId,
            @RequestPart("employee") @Valid EmployeeCreateRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {

        EmployeeDto employeeDto = employeeService.updateEmployee(employeeId,request, photo);

        return ResponseEntity.ok(employeeDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public Employee updateEmployee(
            @PathVariable UUID id,
            @RequestBody Employee employee
    ) {
        return employeeService.updateEmployee(id, employee);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }

    @PostMapping("/export")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<byte[]> exportEmployees(@RequestBody(required = false) EmployeeExportFilter filter) throws IOException {

        byte[] excelFile = employeeService.exportEmployees(filter);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.xls")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(excelFile);
    }
}