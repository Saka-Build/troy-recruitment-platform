package com.troy.ats.searchfilter.dto;

import java.time.OffsetDateTime;

public record ClientFilter(
        String search,
        Boolean active,
        String status,
        String countryCode,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo
) {
}
