package com.meuprojeto.banco.sistemabancario.repository;

import com.meuprojeto.banco.sistemabancario.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    List<Client> findByNameLike(String name);

    List<Client> findByCpfLike(String cpf);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    Optional<Client> findByNameAndBorn(String name, LocalDate nascimento);
    Optional<Client> findByName(String name);
    Optional<Client> findByEmail(String email);
}
