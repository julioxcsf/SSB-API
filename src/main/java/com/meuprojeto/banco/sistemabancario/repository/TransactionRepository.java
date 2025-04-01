package com.meuprojeto.banco.sistemabancario.repository;

import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import com.meuprojeto.banco.sistemabancario.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountOrigin(Account account);
    List<Transaction> findByAccountTarget(Account account);
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
    List<Transaction> findByType(TransactionType type);

    //nome da entidade, não da tabela
    @Query("""
    SELECT t FROM Transaction t
    WHERE (t.accountOrigin = :account OR t.accountTarget = :account)
      AND (:tipo IS NULL OR t.type = :tipo)
      AND (COALESCE(:inicio, t.transactionDate) <= t.transactionDate)
      AND (COALESCE(:fim, t.transactionDate) >= t.transactionDate)
    """)
    List<Transaction> findByAccountWithFilters(
            @Param("account") Account account,
            @Param("tipo") TransactionType tipo,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
