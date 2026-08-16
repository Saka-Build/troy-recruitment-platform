package com.troy.ats.searchfilter.dto;

import com.troy.ats.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeExportFilter {

    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private UserRole role;
    private String designation;
    private Boolean active;
}
