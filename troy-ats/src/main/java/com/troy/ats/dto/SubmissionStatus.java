package com.troy.ats.dto;

import com.troy.ats.enums.CvFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionStatus {

    UUID id;
    String name;
    String colourHex;
    UUID statusIdForSubStatus;
}
