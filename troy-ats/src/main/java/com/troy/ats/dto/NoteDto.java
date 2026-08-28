package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDto {

    private String entityType;
    private UUID entityId;
    private String content;
    private String chatWith;
    private String chatAt;
}
