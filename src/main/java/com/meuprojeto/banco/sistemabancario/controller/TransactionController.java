package com.meuprojeto.banco.sistemabancario.controller;

import com.meuprojeto.banco.sistemabancario.controller.dto.transaction.TransactionDTOResponse;
import com.meuprojeto.banco.sistemabancario.exceptions.AccountUnknownException;
import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import com.meuprojeto.banco.sistemabancario.model.TransactionType;
import com.meuprojeto.banco.sistemabancario.service.AccountService;
import com.meuprojeto.banco.sistemabancario.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("transactions")
public class TransactionController {

    public final TransactionService transactionService;
    public final AccountService accountService;


    @GetMapping("/{number}")
    public ResponseEntity<Object> getTransactionByAccount(@PathVariable String number,
                                                          @RequestParam(required = false, name = "tipo") TransactionType tipo,
                                                          @RequestParam(required = false, name = "inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                                                          @RequestParam(required = false, name = "fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        Long account_number = Long.parseLong(number);
        try {

            Account account = accountService.getAccountByNumber(account_number);

            List<Transaction> transactionList =
                    transactionService.buscarTransacoesComFiltros(account, tipo, inicio, fim);

            List<TransactionDTOResponse> allTransactions = transactionList.stream()
                    .map(TransactionDTOResponse::new)
                    .sorted(Comparator.comparing(TransactionDTOResponse::dataTransacao).reversed())
                    .toList();

            return ResponseEntity.ok(allTransactions);

        } catch (AccountUnknownException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
