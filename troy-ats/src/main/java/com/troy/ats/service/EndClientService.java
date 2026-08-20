package com.troy.ats.service;

import com.troy.ats.dto.EndClientCreateRequest;
import com.troy.ats.dto.EndClientDto;
import com.troy.ats.entity.EndClient;

import java.util.List;
import java.util.UUID;

public interface EndClientService {

    /**
     *
     * @return
     */
    public List<EndClient> getAllEndClients();

    /**
     *
     * @return
     */
    public List<EndClient> getAllActiveEndClients();

    /**
     *
     * @param id
     * @return
     */
    public EndClient getEndClientById(UUID id);

    /**
     *
     * @param id
     */
    public void deleteEndClient(UUID id);


    /**
     *
     * @param request
     * @return
     */
    EndClientDto createEndClient(EndClientCreateRequest request);

    /**
     *
     * @param employeeId
     * @param request
     * @return
     */
    EndClientDto updateEndClient(UUID endClientId, EndClientCreateRequest request);
}