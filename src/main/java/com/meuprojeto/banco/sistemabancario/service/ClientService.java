package com.meuprojeto.banco.sistemabancario.service;

import com.meuprojeto.banco.sistemabancario.model.Client;
import com.meuprojeto.banco.sistemabancario.repository.ClientRepository;
import com.meuprojeto.banco.sistemabancario.validator.ClientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientValidator validator;

    //@annotation do lombok cria para mim
    /*public ClientService(ClientRepository clientRepository,
                            ClientValidator clientValidator) {
        this.clientRepository = clientRepository;
        this.validator = clientValidator;
    }*/

    public Client save(Client client) {
        validator.check(client);
        return clientRepository.save(client);
    }

    public Optional<Client> getById(UUID clientId) {
        return clientRepository.findById(clientId);
    }

    //está meio redundante... poderia usar eraseClient e fazer sobrescrita de função?
    //public void  eraseClient(UUID clientId){}
    //public void  eraseClient(String clientName){}
    public void  deleteClient(Client client) {
        clientRepository.delete(client);
    }

    public List<Client> getByNameFrag(String name) {
        if (name == null || name.isBlank()) {
            return clientRepository.findAll(); // ou outra lógica padrão
        }
        return clientRepository.findByNameLike(name + "%");
    }

    public Optional<Client> getByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return clientRepository.findByName(name);
    }

    public void updateClient(Client client) {
        if(client.getId() == null) {
            throw new IllegalArgumentException("Só é possivel atualizar um cliente já cadastrado.");
        }
        validator.check(client);
        clientRepository.save(client);
    }

}
