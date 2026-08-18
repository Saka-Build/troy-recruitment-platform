package com.troy.ats.dto;

import com.troy.ats.enums.JobStatus;
import com.troy.ats.enums.JobType;
import com.troy.ats.enums.JobWorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private UUID id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String whatsapp;
    private UUID countryId;
    private String countryCode;
    private String countryName;
    private String industry;
    private String status;
    private String address;
    private String notes;
    private Boolean isActive;

}
