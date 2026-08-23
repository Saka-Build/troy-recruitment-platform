package com.troy.ats.service.impl;

import com.troy.ats.dto.*;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.Employee;
import com.troy.ats.entity.Job;
import com.troy.ats.populator.ClientPopulator;
import com.troy.ats.populator.ReverseClientPopulator;
import com.troy.ats.repository.ClientRepository;
import com.troy.ats.searchfilter.dto.ClientExportFilter;
import com.troy.ats.searchfilter.dto.ClientFilter;
import com.troy.ats.searchfilter.filter.ClientSpecification;
import com.troy.ats.searchfilter.filter.JobSpecification;
import com.troy.ats.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service("clientService")
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ReverseClientPopulator reverseClientPopulator;
    private final ClientPopulator clientPopulator;


    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found: " + id));
    }

    @Override
    public Client updateClient(UUID id, Client client) {
       // client.setId(id);
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(UUID id) {
        clientRepository.deleteById(id);
    }

    @Override
    public long getTotalClientsByActive(boolean active) {
        return clientRepository.countByIsActive(active);
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ClientDto createClient(ClientCreateRequest request) {
        validateClientRequest(request);
        Client client = new Client();

        // Populate normal fields
        reverseClientPopulator.populate(request, client);
        client.setIsActive(Boolean.TRUE);

        // Save
        Client savedClient = clientRepository.save(client);

        // Convert entity -> DTO
        ClientDto response = new ClientDto();
        clientPopulator.populate(savedClient, response);

        return response;
    }

    /**
     *
     * @param employeeId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ClientDto updateClient(UUID clientId, ClientCreateRequest request) {

        Client client = getClientById(clientId);

        reverseClientPopulator.populate(request, client);
        // Save employee first
        client = clientRepository.save(client);

        ClientDto clientDto = new ClientDto();
        clientPopulator.populate(client, clientDto);

        return clientDto;
    }

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClientDto> getClients(ClientFilter filter, Pageable pageable) {
        return clientRepository.findAll(ClientSpecification.filter(filter), pageable)
                .map(client -> {
                    ClientDto dto = new ClientDto();
                    clientPopulator.populate(client, dto);
                    return dto;
                });
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public ClientDto getClientDtoById(UUID id) {

        ClientDto clientDto = clientRepository.findById(id)
                .map(client -> {
                    ClientDto dto = new ClientDto();
                    clientPopulator.populate(client, dto);
                    return dto;
                }) .orElseThrow(() -> new EntityNotFoundException("Client not found: " + id));
        return clientDto;
    }

    /**
     *
     * @param filter
     * @return
     * @throws IOException
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] exportClients(ClientExportFilter filter) throws IOException {

        Specification<Client> specification = ClientSpecification.exportFilter(filter);

        List<Client> clients = clientRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "createdAt"));

        return createExcel(clients);
    }

    /**
     *
     * @return
     */
    @Override
    public ClientsFiltersDto getClientFilters() {

        long totalActiveClients = clientRepository.countByIsActive(Boolean.TRUE);
        long totalInActiveClients = clientRepository.countByIsActive(Boolean.FALSE);

        ClientsFiltersDto clientsFiltersDto = new ClientsFiltersDto();
        clientsFiltersDto.setTotalActiveClients(totalActiveClients);
        clientsFiltersDto.setTotalInActiveClients(totalInActiveClients);

        return clientsFiltersDto;

    }

    private byte[] createExcel(List<Client> clients) throws IOException {

        try (Workbook workbook = new HSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Clients");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            headerStyle.setFont(headerFont);

            // Header
            Row header = sheet.createRow(0);

            String[] columns = {
                    "Name",
                    "Contact Person",
                    "Email",
                    "Phone",
                    "WhatsApp",
                    "Country",
                    "Country Code",
                    "Industry",
                    "Status",
                    "Address",
                    "Notes",
                    "Created At",
                    "Updated At"
            };

            for (int i = 0; i < columns.length; i++) {

                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 1;

            for (Client client : clients) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(safe(client.getName()));
                row.createCell(1).setCellValue(safe(client.getContactPerson()));
                row.createCell(2).setCellValue(safe(client.getEmail()));
                row.createCell(3).setCellValue(safe(client.getPhone()));
                row.createCell(4).setCellValue(safe(client.getWhatsapp()));
                row.createCell(5).setCellValue(client.getCountry() != null ? safe(client.getCountry().getName()) : "");
                row.createCell(6).setCellValue(client.getCountry() != null ? safe(client.getCountry().getCode()) : "");
                row.createCell(7).setCellValue(safe(client.getIndustry()));
                row.createCell(8).setCellValue(safe(client.getStatus()));
                row.createCell(9).setCellValue(safe(client.getAddress()));
                row.createCell(10).setCellValue(safe(client.getNotes()));
               // row.createCell(11).setCellValue(Boolean.TRUE.equals(client.getIsActive()));
                row.createCell(12).setCellValue(client.getCreatedAt() != null ? client.getCreatedAt().toString() : "");
                row.createCell(13).setCellValue(client.getUpdatedAt() != null ? client.getUpdatedAt().toString() : "");
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);

            return outputStream.toByteArray();
        }
    }

    private String safe(String value) {
        return value != null ? value : "";
    }

    private String value(String value) {
        return value != null ? value : "";
    }

    private void validateClientRequest(ClientCreateRequest request){

        if (request.getEmail() != null &&
                !request.getEmail().isBlank() &&
                clientRepository.existsByEmailIgnoreCase(request.getEmail())) {

            throw new IllegalArgumentException("Client with email already exists: " + request.getEmail());
        }

    }
}