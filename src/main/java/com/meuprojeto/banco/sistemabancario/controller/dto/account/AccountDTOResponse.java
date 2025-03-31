package com.meuprojeto.banco.sistemabancario.controller.dto.account;

import com.meuprojeto.banco.sistemabancario.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountDTOResponse(
        String nomeCliente,
        Long numeroConta,
        AccountType tipo,
        BigDecimal valor,
        LocalDate createdAt,
        boolean ativa
) {
}
