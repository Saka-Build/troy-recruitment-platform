package com.troy.ats.searchfilter.dto;

import com.troy.ats.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class ClientExportFilter {

    private Boolean active;
    private String status;
    private String countryCode;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}