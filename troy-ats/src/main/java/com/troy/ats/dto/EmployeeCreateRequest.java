package com.troy.ats.dto;

import com.troy.ats.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeCreateRequest {

    private String employeeCode;
    private String fullName;
    private String designation;

    @Email
    private String officialEmail;

    @Email
    private String personalEmail;

    private String phone;
    private String whatsapp;
    private String role;
    private String password;
    private String countryCode;
    private Boolean active;
}