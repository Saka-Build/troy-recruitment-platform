package com.troy.ats.service;

import com.troy.ats.dto.ClientCreateRequest;
import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.JobDto;
import com.troy.ats.entity.Client;

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
}