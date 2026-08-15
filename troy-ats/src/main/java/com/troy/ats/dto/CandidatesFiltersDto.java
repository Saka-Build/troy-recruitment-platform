package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatesFiltersDto {

    Map<String, Long> countByStatus;
    Map<String, List<Map<UUID, String>>> filterDropDowns;

}
