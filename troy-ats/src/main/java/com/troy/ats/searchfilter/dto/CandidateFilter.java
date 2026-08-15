package com.troy.ats.searchfilter.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CandidateFilter(
        String search,
        UUID statusId,
        UUID subStatusId,
        UUID jobId,
        Boolean active,
        String location,
        String source,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo
) {
}
