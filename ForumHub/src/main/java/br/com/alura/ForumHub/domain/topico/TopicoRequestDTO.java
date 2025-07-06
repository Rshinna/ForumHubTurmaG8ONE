package br.com.alura.ForumHub.domain.topico;

import jakarta.validation.constraints.NotBlank;

public record TopicoRequestDTO(
        @NotBlank
        String titulo,
        @NotBlank
        String mensagem,
        @NotBlank
        String nomeCurso,
        Long autorId
) {}



