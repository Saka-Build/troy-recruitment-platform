package com.troy.ats.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientCreateRequest {

    private String name;
    private String contactPerson;

    @Email(message = "Invalid email address")
    private String email;

    private String phone;
    private String whatsapp;
    private String countryCode;
    private String industry;
    private String status;
    private String address;
    private String notes;
    private Boolean isActive;
    private String source;
    private UUID[] endClientIds;

}
