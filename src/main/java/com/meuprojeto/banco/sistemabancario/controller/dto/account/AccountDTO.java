package com.meuprojeto.banco.sistemabancario.controller.dto.account;

import com.meuprojeto.banco.sistemabancario.model.AccountType;

import java.math.BigDecimal;

public record AccountDTO(String clientId,
                         AccountType type,
                         BigDecimal balance,
                         boolean active) {
}
