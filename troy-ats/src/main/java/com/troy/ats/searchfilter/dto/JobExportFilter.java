package com.troy.ats.searchfilter.dto;

import com.troy.ats.enums.JobStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class JobExportFilter {

    private String countryCode;
    private String status;
    private String priority;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}