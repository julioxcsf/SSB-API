package com.meuprojeto.banco.sistemabancario.controller.dto.client;

import com.meuprojeto.banco.sistemabancario.controller.dto.account.AccountDTOResponse;

import java.time.LocalDate;
import java.util.List;

public record ClientDTOResponse(String nome,
                                String cpf,
                                LocalDate nascimento,
                                String email,
                                LocalDate dataCadastro,
                                List<AccountDTOResponse> contas) {
}
