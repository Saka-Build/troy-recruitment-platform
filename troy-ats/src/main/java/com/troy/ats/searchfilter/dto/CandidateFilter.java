package com.troy.ats.searchfilter.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CandidateFilter(
        String search,
        String status,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo
) {
}
