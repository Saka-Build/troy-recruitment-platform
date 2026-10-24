package com.troy.ats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class InterviewScheduleRequest {

    @NotNull(message = "Interview date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate interviewDate;

    @NotNull(message = "Interview time is required")
    private String interviewTime;

    @NotNull(message = "Interview type is required")
    private String interviewType;

    @NotNull(message = "Interview round is required")
    private String round;

    @NotNull(message = "Interviewer is required")
    private String interviewerName;

    @NotNull(message = "Status is required")
    private String status;

    private UUID submissionId;
    private UUID jobId;
    private UUID candidateId;

    private Instant interviewDateTimeWithZone;

    private List<ActivityLogRequest> activityLogs;
}
