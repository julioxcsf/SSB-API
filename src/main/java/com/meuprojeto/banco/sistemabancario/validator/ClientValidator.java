package com.meuprojeto.banco.sistemabancario.validator;

import com.meuprojeto.banco.sistemabancario.exceptions.DuplicatedRegisterException;
import com.meuprojeto.banco.sistemabancario.model.Client;
import com.meuprojeto.banco.sistemabancario.repository.ClientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientValidator {

    private ClientRepository repository;

    public ClientValidator(ClientRepository repository) {
        this.repository = repository;
    }

    public void check(Client client) {
        if(clientExists(client)) {
            throw new DuplicatedRegisterException("Cliente ja cadastrado.");
        }
    }

    private boolean clientExists(Client client) {
        Optional<Client> optionalClient = repository.findByNameAndBorn(
                client.getName(), client.getBorn());

        boolean cpfRegisted = repository.existsByCpf(client.getCpf());
        boolean emailRegisted = repository.existsByEmail(client.getEmail());

        if(client.getId() == null) {
            return (optionalClient.isPresent() || cpfRegisted || emailRegisted);
        }

        return !client.getId().equals(optionalClient.get().getId()) &&
                (optionalClient.isPresent() || cpfRegisted || emailRegisted);
     }
}
