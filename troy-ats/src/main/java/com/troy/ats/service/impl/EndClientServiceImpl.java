package com.troy.ats.service.impl;

import com.troy.ats.dto.ClientCreateRequest;
import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.EndClientCreateRequest;
import com.troy.ats.dto.EndClientDto;
import com.troy.ats.entity.Client;
import com.troy.ats.entity.EndClient;
import com.troy.ats.populator.EndClientPopulator;
import com.troy.ats.populator.ReverseEndClientPopulator;
import com.troy.ats.repository.EndClientRepository;
import com.troy.ats.service.EndClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service("endClientService")
@RequiredArgsConstructor
public class EndClientServiceImpl implements EndClientService {

    private final EndClientRepository endClientRepository;
    private final  EndClientPopulator endClientPopulator;
    private final ReverseEndClientPopulator reverseEndClientPopulator;

    /**
     *
     * @return
     */
    @Override
    public List<EndClient> getAllEndClients() {
        return endClientRepository.findAll();
    }

    /**
     *
     * @return
     */
    @Override
    public List<EndClient> getAllActiveEndClients() {
        return endClientRepository.findByActiveTrueOrderByNameAsc();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public EndClient getEndClientById(UUID id) {
        return endClientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("End Client not found: " + id));
    }

    /**
     *
     * @param id
     */
    @Override
    public void deleteEndClient(UUID id) {

    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public EndClientDto createEndClient(EndClientCreateRequest request) {

        validateEndClientRequest(request);
        EndClient endClient = new EndClient();

        // Populate normal fields
        reverseEndClientPopulator.populate(request, endClient);
        endClient.setActive(Boolean.TRUE);

        // Save
        EndClient savedEndClient = endClientRepository.save(endClient);

        // Convert entity -> DTO
        EndClientDto response = new EndClientDto();
        endClientPopulator.populate(savedEndClient, response);

        return response;

    }

    /**
     *
     * @param endClientId
     * @param request
     * @return
     */
    @Override
    @Transactional
    public EndClientDto updateEndClient(UUID endClientId, EndClientCreateRequest request) {

        EndClient endClient = getEndClientById(endClientId);

        reverseEndClientPopulator.populate(request, endClient);
        // Save employee first
        endClient = endClientRepository.save(endClient);

        EndClientDto endClientDto = new EndClientDto();
        endClientPopulator.populate(endClient, endClientDto);

        return endClientDto;

    }

    private void validateEndClientRequest(EndClientCreateRequest request){

        if (StringUtils.isEmpty(request.getName())) {

            throw new IllegalArgumentException("End Client name is empty ");
        }

    }
}