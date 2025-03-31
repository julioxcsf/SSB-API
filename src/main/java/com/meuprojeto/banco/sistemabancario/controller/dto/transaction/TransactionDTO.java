package com.meuprojeto.banco.sistemabancario.controller.dto.transaction;

import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import com.meuprojeto.banco.sistemabancario.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        Long numeroContaOrigem,
        Long numeroContaDestino,
        TransactionType tipo,
        BigDecimal valor,
        LocalDateTime dataAcao
) {

    public Transaction mapToTransferTransaction(Account origem, Account destino) {
        Transaction tr = new Transaction(origem, destino, valor);
        tr.setTransactionDate(LocalDateTime.now());
        return tr;
    }

    public Transaction mapToGenericTransaction(Account origem) {
        Transaction tr = new Transaction(origem,
                tipo,
                valor);
        tr.setTransactionDate(LocalDateTime.now());
        return tr;
    }
}
