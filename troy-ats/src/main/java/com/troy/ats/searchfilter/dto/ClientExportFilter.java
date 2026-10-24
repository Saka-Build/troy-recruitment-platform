package com.troy.ats.searchfilter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClientExportFilter {

    private Boolean active;
    private String status;
    private String countryCode;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}