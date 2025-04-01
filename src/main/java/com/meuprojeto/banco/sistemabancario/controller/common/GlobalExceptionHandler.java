package com.meuprojeto.banco.sistemabancario.controller.common;

import com.meuprojeto.banco.sistemabancario.controller.dto.ErrorField;
import com.meuprojeto.banco.sistemabancario.controller.dto.ErrorResponse;
import com.meuprojeto.banco.sistemabancario.exceptions.AccountUnknownException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentoNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErrorField> listaErros = fieldErrors.stream().
                map(fe -> new ErrorField(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        System.out.println(e);

        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação de entradas",
                listaErros);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleGenericIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

        if (msg.contains("uuid")) {
            ErrorResponse erro = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Formato de UUID inválido",
                    List.of()
            );
            return ResponseEntity.badRequest().body(erro);
        }

        if (msg.contains("número da conta")) {
            ErrorResponse erro = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.getMessage(),
                    List.of()
            );
            return ResponseEntity.badRequest().body(erro);
        }

        ErrorResponse erro = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                List.of()
        );
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(AccountUnknownException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountUnknownException ex) {
        ErrorResponse erro = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
}
