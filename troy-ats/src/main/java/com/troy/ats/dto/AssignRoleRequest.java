package com.troy.ats.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class AssignRoleRequest {

    @NotNull
    private UUID roleId;

}
