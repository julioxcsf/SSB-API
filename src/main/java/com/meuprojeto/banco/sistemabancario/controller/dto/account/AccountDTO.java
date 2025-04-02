package com.meuprojeto.banco.sistemabancario.controller.dto.account;

import com.meuprojeto.banco.sistemabancario.model.AccountType;

import java.math.BigDecimal;

// Data Transfer Object para recebimento de dados para criação de conta
public record AccountDTO(String clientId,
                         AccountType type,
                         BigDecimal balance,
                         boolean active) {
}
