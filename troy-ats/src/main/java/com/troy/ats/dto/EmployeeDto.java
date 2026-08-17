package com.troy.ats.dto;

import com.troy.ats.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private UUID id;
    private String employeeCode;
    private String fullName;
    private String designation;
    private String officialEmail;
    private String phone;
    private String whatsapp;
    private String photoUrl;
    private UserRole role;
    private LocalDateTime lastLoginAt;
    private Boolean active;
    private CountryDto country;

}
