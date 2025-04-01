package com.meuprojeto.banco.sistemabancario.controller.dto.account;

import com.meuprojeto.banco.sistemabancario.controller.dto.transaction.TransactionDTO;
import com.meuprojeto.banco.sistemabancario.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//nem foi usado - remover no final
public record AccountDTOwithTransactions(
        UUID idCliente,
        String nomeCliente,
        Long numeroConta,
        AccountType tipo,
        BigDecimal valor,
        LocalDate createdAt,
        boolean ativa,
        List<TransactionDTO> extrato
) {
}