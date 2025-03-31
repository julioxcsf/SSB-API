package com.meuprojeto.banco.sistemabancario.repository;

import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByClient(Client client);
    Optional<Account> findByNumber(Long number);
    boolean existsByNumber(Long number);
}
