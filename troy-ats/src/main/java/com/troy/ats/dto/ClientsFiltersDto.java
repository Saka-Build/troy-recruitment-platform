package com.troy.ats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientsFiltersDto {

    long totalClients;
    long totalActiveClients;
    long totalInActiveClients;

}
