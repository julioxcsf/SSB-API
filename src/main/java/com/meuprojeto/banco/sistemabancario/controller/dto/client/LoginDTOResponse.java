package com.meuprojeto.banco.sistemabancario.controller.dto.client;

import lombok.AllArgsConstructor;

import java.util.UUID;

public record LoginDTOResponse(
        String message,
        UUID clientId,
        String name
) {
}
