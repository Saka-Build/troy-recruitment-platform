package com.troy.ats.controller;

import com.troy.ats.dto.ClientCreateRequest;
import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.EndClientCreateRequest;
import com.troy.ats.dto.EndClientDto;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.EndClient;
import com.troy.ats.exception.ApiResponse;
import com.troy.ats.searchfilter.dto.ClientExportFilter;
import com.troy.ats.service.impl.EndClientServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/endclients")
public class EndClientController {

    private final EndClientServiceImpl endClientService;

    public EndClientController(EndClientServiceImpl endClientService) {
        this.endClientService = endClientService;
    }

    @GetMapping("/allEndClients")
    public List<EndClient> getAllEndClients() {
        return endClientService.getAllEndClients();
    }

    @GetMapping("/activeEndClients")
    public List<EndClient> getActiveEndClients() {

        return endClientService.getAllActiveEndClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EndClient> getEndClientById(@PathVariable UUID id) {

        return ResponseEntity.ok(endClientService.getEndClientById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<EndClientDto> createClient(
            @Valid @RequestBody EndClientCreateRequest request) {

        EndClientDto response = endClientService.createEndClient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{endClientId}")
    public ResponseEntity<EndClientDto> updateClient(
            @PathVariable UUID endClientId,
            @RequestBody EndClientCreateRequest request) {

        EndClientDto endclientDto = endClientService.updateEndClient(endClientId, request);

        return ResponseEntity.ok(endclientDto);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteEndClient(@PathVariable UUID id) {

        endClientService.deleteEndClient(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
    }



}