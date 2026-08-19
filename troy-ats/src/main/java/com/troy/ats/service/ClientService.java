package com.troy.ats.service;

import com.troy.ats.dto.ClientCreateRequest;
import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.EmployeeCreateRequest;
import com.troy.ats.dto.EmployeeDto;
import com.troy.ats.entity.Client;
import com.troy.ats.searchfilter.dto.ClientExportFilter;
import com.troy.ats.searchfilter.dto.ClientFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ClientService {

    /**
     *
     * @return
     */
    public List<Client> getAllClients();

    /**
     *
     * @param id
     * @return
     */
    public Client getClientById(UUID id);

    /**
     *
     * @param id
     * @param client
     * @return
     */
    public Client updateClient(UUID id, Client client);

    /**
     *
     * @param id
     */
    public void deleteClient(UUID id);

    /**
     *
     * @param active
     * @return
     */
    public long getTotalClientsByActive(boolean active);

    /**
     *
     * @param request
     * @return
     */
    ClientDto createClient(ClientCreateRequest request);

    /**
     *
     * @param employeeId
     * @param request
     * @return
     */
    ClientDto updateClient(UUID clientId, ClientCreateRequest request);

    /**
     *
     * @param filter
     * @param pageable
     * @return
     */
    Page<ClientDto> getClients(ClientFilter filter, Pageable pageable);

    /**
     *
     * @param id
     * @return
     */
    ClientDto getClientDtoById(UUID id);

    /**
     *
     * @param filter
     * @return
     * @throws IOException
     */
    byte[] exportClients(ClientExportFilter filter) throws IOException;
}