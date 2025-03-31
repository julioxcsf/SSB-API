package com.meuprojeto.banco.sistemabancario.validator;

import com.meuprojeto.banco.sistemabancario.exceptions.OperationNotAllowedException;
import com.meuprojeto.banco.sistemabancario.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidator {

    public void validateTransfer(Account origem, Account destino, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperationNotAllowedException("Valor inválido para transferência.");
        }
        if (origem == null || !origem.isActive()) {
            throw new OperationNotAllowedException("A conta origem está desativada ou nula.");
        }
        if (destino == null || !destino.isActive()) {
            throw new OperationNotAllowedException("A conta destino está desativada ou nula.");
        }
        if (origem.getBalance().compareTo(valor) < 0) {
            throw new OperationNotAllowedException("Saldo insuficiente na conta de origem.");
        }
    }

    public void validateDeposit(Account origem, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperationNotAllowedException("Valor inválido para deposito.");
        }
        if (origem == null || !origem.isActive()) {
            throw new OperationNotAllowedException("A conta origem está desativada ou nula.");
        }
    }

    public void validateWithdraw(Account origem, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new OperationNotAllowedException("Valor inválido para saque.");
        }
        if (origem.getBalance().compareTo(valor) < 0) {
            throw new OperationNotAllowedException("Saldo insuficiente na conta.");
        }
        if (origem == null || !origem.isActive()) {
            throw new OperationNotAllowedException("A conta origem está desativada ou nula.");
        }
    }
}
