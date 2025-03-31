package com.meuprojeto.banco.sistemabancario.exceptions;

public class DuplicatedRegisterException extends RuntimeException{
    public DuplicatedRegisterException(String message) {
        super(message);
    }
}
