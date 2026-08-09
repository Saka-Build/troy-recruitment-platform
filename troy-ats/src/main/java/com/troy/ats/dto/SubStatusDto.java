package com.troy.ats.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubStatusDto {
    private UUID id;
    
    @NotBlank(message = "Status ID is required")
    private UUID statusId;
    
    @NotBlank(message = "Sub-status name is required")
    private String name;
    
    private String colourHex;
    private Short sortOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
}

