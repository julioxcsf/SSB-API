package com.meuprojeto.banco.sistemabancario.service;

import com.meuprojeto.banco.sistemabancario.model.Client;
import com.meuprojeto.banco.sistemabancario.repository.ClientRepository;
import com.meuprojeto.banco.sistemabancario.validator.ClientValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    //injeção pelo Spring
    private final ClientRepository clientRepository;
    private final ClientValidator validator;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct  // Isso vai executar automaticamente ao iniciar a aplicação
    public void testarSenha() {
        String hashDoBanco = "$2a$10$x4ZqOEFCkVEOb/Z7aZy5mejJGVOABEj6g/QZQf3M1F2ZCvNnfQCTm";
        boolean resultado = passwordEncoder.matches("123456789", hashDoBanco);
        System.out.println(resultado ? "✅ Senha bateu!" : "❌ Senha NÃO bateu.");
    }

    public Client save(Client client) {
        validator.check(client);//verifica se o email, cpf e junção nome+dataNascimento ja foram cadastrados

        //Securiry config
        client.setPassword(passwordEncoder.encode(client.getPassword()));

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

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

}
