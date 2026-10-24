package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsFiltersDto {

    long totalJobs;
    long totalOpenJobs;
    long totalClosedJobs;
    long totalOnHoldJobs;
    List<String> statuses;
    List<String> priorities;

}
