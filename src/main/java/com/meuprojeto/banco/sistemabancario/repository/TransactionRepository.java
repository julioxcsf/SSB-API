package com.meuprojeto.banco.sistemabancario.repository;

import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountOrigin(Account Account);
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}
