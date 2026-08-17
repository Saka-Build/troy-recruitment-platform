package com.troy.ats.dto;

import com.troy.ats.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeCreateRequest {

    @Size(max = 50)
    private String employeeCode;

    @Size(max = 255)
    private String fullName;

    @Size(max = 150)
    private String designation;

    @Email
    @Size(max = 255)
    private String officialEmail;

    @Email
    @Size(max = 255)
    private String personalEmail;

    @Size(max = 30)
    private String phone;

    @Size(max = 30)
    private String whatsapp;

    private String role;

    @Size(min = 8, max = 100)
    private String password;

    @Size(min = 2, max = 2)
    private String countryCode;

    private Boolean active;
}