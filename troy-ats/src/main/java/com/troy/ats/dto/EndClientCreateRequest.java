package com.troy.ats.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndClientCreateRequest {

    private String name;
    private Boolean active;

}
