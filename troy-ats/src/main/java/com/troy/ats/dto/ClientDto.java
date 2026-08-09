package com.troy.ats.dto;

import com.troy.ats.enums.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private UUID id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String whatsapp;
    private String country;
    private String industry;
    private String status;
    private String address;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private String createdByName;
}

