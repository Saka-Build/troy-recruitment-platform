package com.troy.ats.service.impl;

import com.troy.ats.dto.ClientCreateRequest;
import com.troy.ats.dto.ClientDto;
import com.troy.ats.dto.JobDto;
import com.troy.ats.entity.Client;
import com.troy.ats.populator.ClientPopulator;
import com.troy.ats.populator.ReverseClientPopulator;
import com.troy.ats.repository.ClientRepository;
import com.troy.ats.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public ClientDto createClient(ClientCreateRequest request) {
        validateClientRequest(request);
        Client client = new Client();

        // Populate normal fields
        reverseClientPopulator.populate(request, client);

        // Save
        Client savedClient = clientRepository.save(client);

        // Convert entity -> DTO
        ClientDto response = new ClientDto();
        clientPopulator.populate(savedClient, response);

        return response;
    }

    private void validateClientRequest(ClientCreateRequest request){

        if (request.getEmail() != null &&
                !request.getEmail().isBlank() &&
                clientRepository.existsByEmailIgnoreCase(request.getEmail())) {

            throw new IllegalArgumentException("Client with email already exists: " + request.getEmail());
        }

    }
}