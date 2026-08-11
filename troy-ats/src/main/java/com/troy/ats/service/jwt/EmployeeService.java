package com.troy.ats.service.jwt;

import com.troy.ats.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * employee service
 */
@Service
public interface EmployeeService {

    /** Get employee by email id
     * @param emailId
     * @return
     * */
    Employee findByEmailId(String emailId);

    /** Get employee by id
     * @param id
     * @return
     * */
    Employee findById(UUID id);

    /** update employee
     *
     * @param employee
     */
    void updateEmployee(Employee employee);
}
