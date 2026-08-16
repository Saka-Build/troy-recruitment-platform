package com.troy.ats.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class EmployeeExportRequest {

    private LocalDate fromDate;
    private LocalDate toDate;
    private String role;
    private String designation;
    private Boolean active;

}
