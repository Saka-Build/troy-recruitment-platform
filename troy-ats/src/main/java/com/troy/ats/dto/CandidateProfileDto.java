package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDto {

    String cvId;
    String cvOwner;
    String referredBy;
    String email;
    String phone;
    String whatsapp;
    String location;
    String[] skills;
    OffsetDateTime createdAt;
}
