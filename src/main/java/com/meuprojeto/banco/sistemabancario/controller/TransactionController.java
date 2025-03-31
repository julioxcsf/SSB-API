package com.meuprojeto.banco.sistemabancario.controller;

import com.meuprojeto.banco.sistemabancario.controller.dto.transaction.TransactionDTOResponse;
import com.meuprojeto.banco.sistemabancario.exceptions.AccountUnknownException;
import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import com.meuprojeto.banco.sistemabancario.service.AccountService;
import com.meuprojeto.banco.sistemabancario.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") // Libera para qualquer origem (somente para teste)
@RequiredArgsConstructor
@RestController
@RequestMapping("transactions")
public class TransactionController {

    public final TransactionService transactionService;
    public final AccountService accountService;

    @GetMapping("/{number}")
    public ResponseEntity<Object> getTransactionByAccount(@PathVariable String number) {
        Long account_number = Long.parseLong(number);
        try {

            Account account = accountService.getAccountByNumber(account_number);

            List<Transaction> transactionList =
                        transactionService.findByAccount(account);

            if(transactionList.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<TransactionDTOResponse> transactions = transactionList.stream().map(
                    tr -> new TransactionDTOResponse(tr.getAccountOrigin(),
                            tr.getAccountTarget(),
                            tr.getType(),
                            tr.getValue(),
                            tr.getTransactionDate())).toList();
            return ResponseEntity.ok(transactions);

        } catch (AccountUnknownException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
