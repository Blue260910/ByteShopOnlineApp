package br.com.fiap.byteshoponlineapp.api.dto;

/** Payload de saída (resposta JSON) */
public record ClienteResponse(
        Long id,
        String nome,
        String email,
        String documento
) {}