package com.troy.ats.dto;

import com.troy.ats.enums.InterviewType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDataForDashboardDto {

    private String interviewerName;
    private String candidateName;
    private String skillName;
    private String jobName;
    private InterviewType interviewType;
    private LocalTime interviewTime;
    private LocalDate interviewDate;
    private String interviewLink;

}
