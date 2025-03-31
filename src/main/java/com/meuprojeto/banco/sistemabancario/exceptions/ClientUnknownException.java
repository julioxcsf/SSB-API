package com.meuprojeto.banco.sistemabancario.exceptions;

public class ClientUnknownException extends RuntimeException{
    public ClientUnknownException(String message) {
        super(message);
    }
}
