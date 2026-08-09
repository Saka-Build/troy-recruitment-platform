package com.troy.ats.service;

import com.troy.ats.entity.Client;
import com.troy.ats.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(UUID id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(UUID id, Client client) {
       // client.setId(id);
        return clientRepository.save(client);
    }

    public void deleteClient(UUID id) {
        clientRepository.deleteById(id);
    }
}