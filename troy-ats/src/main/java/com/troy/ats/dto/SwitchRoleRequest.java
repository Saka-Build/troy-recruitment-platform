package com.troy.ats.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SwitchRoleRequest {

    @NotNull
    private UUID roleId;
}