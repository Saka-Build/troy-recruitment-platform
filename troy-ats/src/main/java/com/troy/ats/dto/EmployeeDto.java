package com.troy.ats.dto;

import com.troy.ats.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {
    private UUID id;
    
    @NotBlank(message = "Employee code is required")
    private String employeeCode;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Designation is required")
    private String designation;
    
    @NotBlank(message = "Official email is required")
    @Email(message = "Invalid email format")
    private String officialEmail;
    
    private String personalEmail;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;
    
    @NotBlank(message = "WhatsApp is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid WhatsApp number format")
    private String whatsapp;
    
    private String photoUrl;
    private UserRole role;
    private Boolean isActive;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

