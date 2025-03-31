package com.meuprojeto.banco.sistemabancario.service;

import com.meuprojeto.banco.sistemabancario.exceptions.AccountUnknownException;
import com.meuprojeto.banco.sistemabancario.model.*;
import com.meuprojeto.banco.sistemabancario.repository.AccountRepository;
import com.meuprojeto.banco.sistemabancario.repository.ClientRepository;
import com.meuprojeto.banco.sistemabancario.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final Random random = new Random();

    public Long gerarNumeroContaUnico() {
        Long numero;
        do {
            numero = 10000L + random.nextInt(90000); // número entre 10000 e 99999
        } while (accountRepository.existsByNumber(numero));
        return numero;
    }

    public AccountService(AccountRepository accountRepository,
                          ClientRepository clientRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account criarConta(Account account) {
        account.setNumber(gerarNumeroContaUnico());
        return accountRepository.save(account);
    }

    public Account atualizarConta(Account account) {
        return accountRepository.save(account);
    }


    public List<Account> findClientAccounts(Client client) {
        return accountRepository.findByClient(client);
    }

    public Account getAccountByNumber(Long number) {
        return accountRepository.findByNumber(number)
                .orElseThrow(() -> new AccountUnknownException("Conta não encontrada."));
    }
}
