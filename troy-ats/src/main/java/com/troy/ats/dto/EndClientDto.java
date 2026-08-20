package com.troy.ats.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EndClientDto {

    private UUID id;
    private String name;
    private Boolean active;

}
