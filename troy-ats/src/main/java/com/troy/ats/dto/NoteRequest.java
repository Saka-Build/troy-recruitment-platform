package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequest {

    private String entityType;
    private UUID entityId;
    private String content;
    private Boolean pinned;
}
