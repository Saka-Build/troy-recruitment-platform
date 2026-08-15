package com.troy.ats.dto;

import com.troy.ats.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeCreateRequest {

    @NotBlank
    @Size(max = 50)
    private String employeeCode;

    @NotBlank
    @Size(max = 255)
    private String fullName;

    @NotBlank
    @Size(max = 150)
    private String designation;

    @NotBlank
    @Email
    @Size(max = 255)
    private String officialEmail;

    @Email
    @Size(max = 255)
    private String personalEmail;

    @NotBlank
    @Size(max = 30)
    private String phone;

    @NotBlank
    @Size(max = 30)
    private String whatsapp;

    @NotNull
    private String role;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    private Boolean isActive = true;

    @NotBlank
    @Size(min = 2, max = 2)
    private String countryCode;
}