package com.troy.ats.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SubmissionStatusRequest {

    private UUID statusId;
    String name;
    String colourHex;
    String sortOrder;
    Boolean active;


}
