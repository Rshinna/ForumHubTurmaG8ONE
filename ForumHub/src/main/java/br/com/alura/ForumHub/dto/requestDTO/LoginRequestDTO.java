package br.com.alura.ForumHub.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String login,
        @NotBlank String senha
) {}
