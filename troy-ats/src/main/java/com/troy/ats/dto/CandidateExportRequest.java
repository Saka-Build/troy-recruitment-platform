package com.troy.ats.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Data
public class CandidateExportRequest {

    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
    private String skill;
    private String location;
    private Boolean active;
    private UUID statusId;

}
