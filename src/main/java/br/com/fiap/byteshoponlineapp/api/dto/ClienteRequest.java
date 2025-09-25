package br.com.fiap.byteshoponlineapp.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Payload de entrada (POST/PUT) — simples para aula */
public record ClienteRequest(
        @NotBlank String nome,
        @Email @NotBlank String email,
        @NotBlank String documento
) {}
