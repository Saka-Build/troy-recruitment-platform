package com.troy.ats.searchfilter.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SubmissionFilter(
        String search,
        String pipelineStage,
        String statusName,
        String subStatusName,
        UUID candidateId,
        UUID jobId,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo
) {
}
