package com.troy.ats.searchfilter.dto;

import java.time.OffsetDateTime;

public record EmployeeFilter(
        String search,
        Boolean active,
        String location,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo
) {
}
