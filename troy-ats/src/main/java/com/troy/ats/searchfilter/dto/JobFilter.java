package com.troy.ats.searchfilter.dto;

import com.troy.ats.enums.JobStatus;

import java.time.OffsetDateTime;

public record JobFilter(
        String search,
        String countryCode,
        JobStatus status,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo
) {
}
