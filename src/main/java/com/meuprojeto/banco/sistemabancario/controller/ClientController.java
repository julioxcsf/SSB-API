package com.meuprojeto.banco.sistemabancario.controller;

import com.meuprojeto.banco.sistemabancario.controller.dto.*;
import com.meuprojeto.banco.sistemabancario.controller.dto.account.AccountDTOResponse;
import com.meuprojeto.banco.sistemabancario.controller.dto.client.*;
import com.meuprojeto.banco.sistemabancario.controller.dto.validationGroup.OnCreate;
import com.meuprojeto.banco.sistemabancario.controller.dto.validationGroup.OnUpdateEmail;
import com.meuprojeto.banco.sistemabancario.controller.dto.validationGroup.OnUpdatePassword;
import com.meuprojeto.banco.sistemabancario.exceptions.DuplicatedRegisterException;
import com.meuprojeto.banco.sistemabancario.model.Account;
import com.meuprojeto.banco.sistemabancario.model.Client;
import com.meuprojeto.banco.sistemabancario.service.AccountService;
import com.meuprojeto.banco.sistemabancario.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Object> register(@RequestBody @Validated(OnCreate.class)
                                           ClientDTO clientDTO) {
        try {
            Client client = clientDTO.mapToClient();
            service.save(client);

            //cria o uri para visualizar o novo cliente
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{clientId}")
                    .buildAndExpand(client.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (DuplicatedRegisterException e) {
            ErrorResponse error = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDTOResponse> getInfo(@PathVariable String clientId) {
        UUID id = UUID.fromString(clientId);
        Optional<Client> optionalClient = service.getById(id);
        if(optionalClient.isPresent()) {
            Client client = optionalClient.get();
            List<Account> accounts = accountService.findClientAccounts(client);

            List<AccountDTOResponse> accountDTOS = accounts.stream().map(acc -> new AccountDTOResponse(
                    acc.getClient().getId(),
                    acc.getClient().getName(),
                    acc.getNumber(),
                    acc.getType(),
                    acc.getBalance(),
                    acc.getCreatedAt(),
                    acc.isActive()
            )).toList();

            //falta passar as contas para account DTOs...
            ClientDTOResponse dto = new ClientDTOResponse(client.getName(),
                    client.getCpf(),client.getBorn(),
                    client.getEmail(),client.getCreatedAt(),
                    accountDTOS);
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientDTOResponseId> getIdByEmail(@PathVariable String email) {
        UUID id = service.getClientByEmail(email).get().getId();
        ClientDTOResponseId dto = new ClientDTOResponseId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDTOResponse>> getInfoByName
            (@RequestParam(value = "nome", required = false) String nome) {
        List<Client> clientList = service.getByNameFrag(nome);
        if (clientList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<ClientDTOResponse> dtoResponseList = clientList.stream()
                .map(client -> new ClientDTOResponse(client.getName(),
                        client.getCpf(), client.getBorn(),
                        client.getEmail(), client.getCreatedAt(),
                        null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoResponseList);
    }

    //pois não vai nada no body
    @DeleteMapping("{clientId}")
    public ResponseEntity<Void> delete(@PathVariable String clientId) {
        UUID id = UUID.fromString(clientId);
        Optional<Client> optionalClient = service.getById(id);

        if(optionalClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteClient(optionalClient.get());
        return ResponseEntity.noContent().build();
    }

    //pois não vai nada no body
    @DeleteMapping("remove/{name}")
    public ResponseEntity<Void> deleteByName(@PathVariable String name) {
        Optional<Client> optionalClient = service.getByName(name);

        if(optionalClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteClient(optionalClient.get());
        return ResponseEntity.noContent().build();
    }

    //eu ainda nao tratei a atualização para não usar email ja registrado...
    @PutMapping("{clientId}/email")
    public ResponseEntity<Void> updateEmail(@PathVariable String clientId,
                 @RequestBody @Validated(OnUpdateEmail.class) ClientDTO clientDTO) {
        UUID id = UUID.fromString(clientId);
        Optional<Client> optionalClient = service.getById(id);

        if(optionalClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = optionalClient.get();
        client.setEmail(clientDTO.email());
        service.updateClient(client);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{clientId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable String clientId,
                  @RequestBody @Validated(OnUpdatePassword.class) ClientDTO clientDTO) {
        UUID id = UUID.fromString(clientId);
        Optional<Client> optionalClient = service.getById(id);

        if(optionalClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = optionalClient.get();
        client.setEmail(clientDTO.senha_hash());
        service.updateClient(client);

        return ResponseEntity.noContent().build();
    }

}
