package com.meuprojeto.banco.sistemabancario.exceptions;

public class AccountUnknownException extends RuntimeException{
    public AccountUnknownException(String message) {
        super(message);
    }
}
