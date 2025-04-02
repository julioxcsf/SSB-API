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

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


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
            // Validação do UUID
            if (accountDTO.clientId() == null || accountDTO.clientId().isBlank()) {
                return ResponseEntity.badRequest().body("ID do cliente é obrigatório.");
            }

            UUID clientUUID;
            try {
                clientUUID = UUID.fromString(accountDTO.clientId());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Formato de UUID inválido para o cliente.");
            }

            if (accountDTO.balance().compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "O saldo inicial não pode ser negativo."));
            }

            Optional<Client> client = clientService.getById(clientUUID);
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
    public ResponseEntity<?> getAccountInfo(@PathVariable String number) {
        try {
            if (number == null || number.isBlank()) {
                throw new IllegalArgumentException("Número da conta está vazio ou ausente.");
            }

            Long accountNumber = Long.parseLong(number);
            Account account = accountService.getAccountByNumber(accountNumber);

            AccountDTOResponse dtoResponse = new AccountDTOResponse(
                    account.getClient().getId(),
                    account.getClient().getName(),
                    account.getNumber(),
                    account.getType(),
                    account.getBalance(),
                    account.getCreatedAt(),
                    account.isActive());

            return ResponseEntity.ok(dtoResponse);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número da conta inválido. Deve conter apenas dígitos.");
        }
    }

    @Transactional
    @PutMapping("/{number}/transfer")
    public ResponseEntity<Object> transfer(@PathVariable String number,@RequestBody TransactionDTO dto) {
        try {
            if (number == null || number.isBlank()) {
                throw new IllegalArgumentException("Número da conta está vazio ou ausente.");
            }
            Long accountNumber = Long.parseLong(number);

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
        } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Número da conta inválido. Deve conter apenas dígitos.");
    }
    }

    @Transactional
    @PutMapping("/{number}/deposit")
    public ResponseEntity<Object> deposit(@PathVariable String number,@RequestBody TransactionDTO dto) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Número da conta está vazio ou ausente.");
        }
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
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Número da conta está vazio ou ausente.");
        }
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

    @Transactional
    @PutMapping("/{number}/toggle-status")
    public ResponseEntity<?> toggleAccountStatus(@PathVariable String number) {
        try {
            Long accountNumber = Long.parseLong(number);
            Account account = accountService.getAccountByNumber(accountNumber);
            account.setActive(!account.isActive());
            accountService.atualizarConta(account);
            return ResponseEntity.ok().body("Status da conta atualizado com sucesso.");
        } catch (AccountUnknownException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar status da conta.");
        }
    }
}
