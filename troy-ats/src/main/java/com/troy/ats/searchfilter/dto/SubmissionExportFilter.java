package com.troy.ats.searchfilter.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class SubmissionExportFilter {

    private UUID statusId;
    private List<UUID> statusIds;
    private UUID jobId;
    private UUID clientId;
    private OffsetDateTime createdFrom;
    private OffsetDateTime createdTo;
}
