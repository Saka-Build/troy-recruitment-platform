package com.troy.ats.controller;

import com.troy.ats.dto.ClientCreateRequest;
import com.troy.ats.dto.ClientDto;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.EndClient;
import com.troy.ats.searchfilter.dto.ClientExportFilter;
import com.troy.ats.searchfilter.dto.ClientFilter;
import com.troy.ats.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/allClients")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> getClients(@RequestParam(required = false) String search,
                                                          @RequestParam(required = false) Boolean active,
                                                          @RequestParam(required = false) String countryCode,
                                                          @RequestParam(required = false) OffsetDateTime createdFrom,
                                                          @RequestParam(required = false) OffsetDateTime createdTo,
                                                          @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ClientFilter filter = new ClientFilter(search, active, countryCode, createdFrom, createdTo);

        return ResponseEntity.ok(clientService.getClients(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable UUID id) {

        return ResponseEntity.ok(clientService.getClientDtoById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ClientDto> createClient(
            @Valid @RequestBody ClientCreateRequest request) {

        ClientDto response = clientService.createClient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{clientId}")
    public ResponseEntity<ClientDto> updateClient(
            @PathVariable UUID clientId,
            @RequestBody ClientCreateRequest request) {

        ClientDto clientDto = clientService.updateClient(clientId, request);

        return ResponseEntity.ok(clientDto);
    }


    @PutMapping("/{id}")
    public Client updateClient(@PathVariable UUID id, @RequestBody Client client) {
       // client.setId(id);
        return clientService.updateClient(id, client);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportClients(@RequestBody(required = false) ClientExportFilter filter) throws IOException {

        byte[] excelFile = clientService.exportClients(filter);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=clients.xls")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(excelFile);
    }

}