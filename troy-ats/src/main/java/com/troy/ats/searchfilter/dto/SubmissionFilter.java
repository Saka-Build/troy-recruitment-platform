package com.troy.ats.searchfilter.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record SubmissionFilter(
        String search,
        String pipelineStage,
        UUID statusId,
        List<UUID> statusIds,
        String statusName,
        String subStatusName,
        UUID candidateId,
        UUID jobId,
        UUID clientId,
        OffsetDateTime createdFrom,
        OffsetDateTime createdTo
) {
}
