package com.meuprojeto.banco.sistemabancario.controller.dto.account;

import com.meuprojeto.banco.sistemabancario.controller.dto.transaction.TransactionDTO;
import com.meuprojeto.banco.sistemabancario.model.AccountType;
import com.meuprojeto.banco.sistemabancario.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AccountDTOwithTransactions(
        String nomeCliente,
        Long numeroConta,
        AccountType tipo,
        BigDecimal valor,
        LocalDate createdAt,
        boolean ativa,
        List<TransactionDTO> extrato
) {
}