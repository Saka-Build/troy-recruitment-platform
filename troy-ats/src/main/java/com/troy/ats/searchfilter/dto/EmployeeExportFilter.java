package com.troy.ats.searchfilter.dto;

import com.troy.ats.enums.RoleName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmployeeExportFilter {

    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private RoleName role;
    private String designation;
    private Boolean active;
}
