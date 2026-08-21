package com.troy.ats.searchfilter.dto;

import com.troy.ats.enums.JobStatus;

import java.time.OffsetDateTime;

public record JobFilter(
        String search,
        String countryCode,
        String status,
        String priority,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo
) {
}
