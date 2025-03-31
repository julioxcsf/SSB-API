package com.meuprojeto.banco.sistemabancario.service;

import com.meuprojeto.banco.sistemabancario.exceptions.ClientUnknownException;
import com.meuprojeto.banco.sistemabancario.exceptions.OperationNotAllowedException;
import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import com.meuprojeto.banco.sistemabancario.repository.AccountRepository;
import com.meuprojeto.banco.sistemabancario.repository.TransactionRepository;
import com.meuprojeto.banco.sistemabancario.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionValidator validator;



    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findByAccount(Account account) {
        return transactionRepository.findByAccountOrigin(account);
    }

    public void executeTransfer(Account origin, Account target, BigDecimal amount) {
        validator.validateTransfer(origin, target, amount);

        origin.setBalance(origin.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));

        accountRepository.save(origin);
        accountRepository.save(target);
    }

    public void executeDeposit(Account account, BigDecimal amount) {
        validator.validateDeposit(account, amount);
        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);
    }

    public void executeWithdraw(Account account, BigDecimal amount) {
        validator.validateWithdraw(account, amount);
        account.setBalance(account.getBalance().subtract(amount));

        accountRepository.save(account);
    }
}
