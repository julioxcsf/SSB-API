package com.meuprojeto.banco.sistemabancario.controller.dto.transaction;

import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import com.meuprojeto.banco.sistemabancario.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTOResponse(
        Account contaOrigem,
        Account contaDestino,
        TransactionType tipo,
        BigDecimal valor,
        LocalDateTime dataTransacao
) {
    @Override
    public LocalDateTime dataTransacao() {
        return dataTransacao;
    }

    public TransactionDTOResponse(Transaction tr) {
        this(tr.getAccountOrigin(),
                tr.getAccountTarget(),
                tr.getType(),
                tr.getValue(),
                tr.getTransactionDate());
    }
}
