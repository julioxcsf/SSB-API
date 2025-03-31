package com.meuprojeto.banco.sistemabancario.repository;

import com.meuprojeto.banco.sistemabancario.controller.dto.client.ClientDTO;
import com.meuprojeto.banco.sistemabancario.model.Client;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get all users starting with 'char'")
    void findByNameLikeSucces() {
        var data = LocalDate.of(2000,7,15);
        ClientDTO dto = new ClientDTO("Joao Teste",
                "12345678910",data,
                "tentando@gmail.com",
                "OASIdh239daODISN9409jfDPJI");
        this.createClient(dto);

        Optional<List<Client>> result = Optional.ofNullable(this.clientRepository.findByNameLike("J"));

        assertThat(result.isPresent()); // Testa se a lista foi encontrada
    }

    @Test
    @DisplayName("Should not get users starting with 'char'")
    void findByNameLikeFail() {
        Optional<List<Client>> result = Optional.ofNullable(this.clientRepository.findByNameLike("Z"));

        assertThat(result.isEmpty()); // Testa se a lista foi encontrada
    }

    private Client createClient(ClientDTO data) {
        Client client = data.mapToClient();
        this.entityManager.persist(client);
        return client;
    }
}