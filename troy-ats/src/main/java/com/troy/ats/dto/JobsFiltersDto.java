package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsFiltersDto {

    long totalOpenJobs;
    long totalClosedJobs;
    long totalOnHoldJobs;

}
