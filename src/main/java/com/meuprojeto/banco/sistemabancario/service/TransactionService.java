package com.meuprojeto.banco.sistemabancario.service;

import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import com.meuprojeto.banco.sistemabancario.model.TransactionType;
import com.meuprojeto.banco.sistemabancario.repository.AccountRepository;
import com.meuprojeto.banco.sistemabancario.repository.TransactionRepository;
import com.meuprojeto.banco.sistemabancario.validator.TransactionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionValidator validator;



    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    //retorna as transações que a conta é a origem
    public List<Transaction> findByOrigin(Account account) {
        return transactionRepository.findByAccountOrigin(account);
    }

    //retorna as transações que a conta é o destino
    public List<Transaction> findByTarget(Account account) {
        return  transactionRepository.findByAccountTarget(account);
    }

    public List<Transaction> buscarTransacoesComFiltros(Account account,
                                                        TransactionType tipo,
                                                        LocalDateTime inicio,
                                                        LocalDateTime fim) {
        return transactionRepository.findByAccountWithFilters(account, tipo, inicio, fim);
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
