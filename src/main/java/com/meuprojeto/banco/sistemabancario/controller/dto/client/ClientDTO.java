package com.meuprojeto.banco.sistemabancario.controller.dto.client;

import com.meuprojeto.banco.sistemabancario.controller.dto.validationGroup.OnCreate;
import com.meuprojeto.banco.sistemabancario.controller.dto.validationGroup.OnUpdateEmail;
import com.meuprojeto.banco.sistemabancario.controller.dto.validationGroup.OnUpdatePassword;
import com.meuprojeto.banco.sistemabancario.model.Client;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

//data transfer object
//por ser mais sucetivel a erros, foi o foco de verificações de entradas
public record ClientDTO (
        @NotBlank(message = "Nome é obrigatório", groups = OnCreate.class)
        @Size(min = 2, max = 100, message = "Comprimento de nome inválido", groups = OnCreate.class)
        String nome,

        @NotBlank(message = "CPF é obrigatório", groups = OnCreate.class)
        //@Size( min = 11, max = 11, message = "CPF deve possuir exatamente 11 digitos <11122233345>",
         //       groups = OnCreate.class)
        @CPF(groups = OnCreate.class)
        String cpf,

        @NotNull(message = "Data de nascimento é obrigatória", groups = OnCreate.class)
        @Past(message = "Data inválida.",groups = OnCreate.class)
        LocalDate nascimento,

        @NotBlank(message = "Email é obrigatório", groups = {OnCreate.class, OnUpdateEmail.class})
        @Email(message = "Email inválido", groups = {OnCreate.class, OnUpdateEmail.class})
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com|br)$",
                message = "Email deve terminar em .com ou .br",
                groups = {OnCreate.class, OnUpdateEmail.class}
        )
        String email,

        @NotBlank(message = "Senha é obrigatória", groups = {OnCreate.class, OnUpdatePassword.class})
        @Size(min = 8, max = 20, message = "A senha deve ter de 8 a 20 caracteres.",
                groups = {OnCreate.class, OnUpdatePassword.class})
        String senha_hash) {

    public Client mapToClient() {
        Client client = new Client();
        client.setName(nome);
        client.setCpf(cpf);
        client.setBorn(nascimento);
        client.setEmail(email);
        client.setPassword(senha_hash);
        return client;
    }
}
