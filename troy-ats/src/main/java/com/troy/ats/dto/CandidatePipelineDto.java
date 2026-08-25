package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatePipelineDto {

    UUID id;
    String fullName;
    String email;
    String source;
    String currentDesignation;
    String jobTitle;

}
