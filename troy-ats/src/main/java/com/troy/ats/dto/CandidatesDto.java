package com.troy.ats.dto;

import com.troy.ats.enums.CvFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatesDto {
    UUID id;
    String fullName;
    String email;
    String whatsapp;
    String location;
    String currentDesignation;
    BigDecimal experienceYears;
    String[] skills;
    UUID statusId;
    String statusName;
    String statusColour;
    UUID subStatusId;
    String subStatusName;
    Boolean active;
    OffsetDateTime createdAt;
}
