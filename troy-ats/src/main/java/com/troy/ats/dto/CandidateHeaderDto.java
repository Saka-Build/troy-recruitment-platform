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
public class CandidateHeaderDto {
    UUID id;
    String fullName;
    String location;
    String currentDesignation;
    Boolean active;
    UUID statusId;
    String statusName;
    String statusColour;
    UUID subStatusId;
    String subStatusName;
}
