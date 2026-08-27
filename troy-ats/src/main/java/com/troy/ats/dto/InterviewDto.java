package com.troy.ats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Setter
@Getter
public class InterviewDto {

    UUID id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate interviewDate;

    private String interviewTime;

    private String interviewType;
    private String round;
    private String interviewerName;
    private String status;

    //private UUID submissionId;
    //private UUID jobId;
    //private UUID candidateId;
    

}
