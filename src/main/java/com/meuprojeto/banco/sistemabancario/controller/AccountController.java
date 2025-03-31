package com.meuprojeto.banco.sistemabancario.controller;

import com.meuprojeto.banco.sistemabancario.controller.dto.account.AccountDTO;
import com.meuprojeto.banco.sistemabancario.controller.dto.account.AccountDTOResponse;
import com.meuprojeto.banco.sistemabancario.controller.dto.ErrorResponse;
import com.meuprojeto.banco.sistemabancario.controller.dto.transaction.TransactionDTO;
import com.meuprojeto.banco.sistemabancario.controller.dto.validationGroup.OnCreate;
import com.meuprojeto.banco.sistemabancario.exceptions.AccountUnknownException;
import com.meuprojeto.banco.sistemabancario.exceptions.ClientUnknownException;
import com.meuprojeto.banco.sistemabancario.exceptions.OperationNotAllowedException;
import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Client;
import com.meuprojeto.banco.sistemabancario.model.Transaction;
import com.meuprojeto.banco.sistemabancario.service.AccountService;
import com.meuprojeto.banco.sistemabancario.service.ClientService;
import com.meuprojeto.banco.sistemabancario.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@CrossOrigin(origins = "*") // Libera para qualquer origem (somente para teste)
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Object> createAccount(@RequestBody @Validated(OnCreate.class)
                                                    AccountDTO accountDTO) {
        try {
            Optional<Client> client = clientService.getById(UUID.fromString(accountDTO.clientId()));
            if(client.isEmpty()) {
                throw new ClientUnknownException("Cliente não cadastrado.");
            }
            Account account = new Account();
            account.setClient(client.get());
            account.setType(accountDTO.type());
            account.setBalance(accountDTO.balance());
            account.setActive(accountDTO.active());
            accountService.criarConta(account);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (ClientUnknownException e) {
            ErrorResponse error = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }
    }

    @GetMapping("/{number}")
    public ResponseEntity<AccountDTOResponse> getAccountInfo(@PathVariable String number) {
        Long account_number = Long.parseLong(number);
        Account account = accountService.getAccountByNumber(account_number);

        AccountDTOResponse dtoResponse = new AccountDTOResponse(
                            account.getClient().getName(),
                            account.getNumber(),
                            account.getType(),
                            account.getBalance(),
                            account.getCreatedAt(),
                            account.isActive());
        return ResponseEntity.ok(dtoResponse);
    }

    @Transactional
    @PutMapping("/{number}/transfer")
    public ResponseEntity<Object> transfer(@PathVariable String number,@RequestBody TransactionDTO dto) {
        Long accountNumber = Long.parseLong(number);
        try {
            Account accountOrigin = accountService.getAccountByNumber(accountNumber);
            Account accountTarget = accountService.getAccountByNumber(dto.numeroContaDestino());

            transactionService.executeTransfer(accountOrigin,accountTarget,dto.valor());

            Transaction transaction = dto.mapToTransferTransaction(accountOrigin, accountTarget);
            transactionService.saveTransaction(transaction);

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (AccountUnknownException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (OperationNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Transactional
    @PutMapping("/{number}/deposit")
    public ResponseEntity<Object> deposit(@PathVariable String number,@RequestBody TransactionDTO dto) {
        Long accountNumber = Long.parseLong(number);
        try {
            Account account = accountService.getAccountByNumber(accountNumber);

            transactionService.executeDeposit(account, dto.valor());

            transactionService.saveTransaction(dto.mapToGenericTransaction(account));

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (AccountUnknownException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (OperationNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Transactional
    @PutMapping("/{number}/withdraw")
    public ResponseEntity<Object> withdraw(@PathVariable String number,@RequestBody TransactionDTO dto) {
        Long accountNumber = Long.parseLong(number);
        try {
            Account account = accountService.getAccountByNumber(accountNumber);

            transactionService.executeWithdraw(account, dto.valor());

            transactionService.saveTransaction(dto.mapToGenericTransaction(account));

            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (AccountUnknownException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (OperationNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
