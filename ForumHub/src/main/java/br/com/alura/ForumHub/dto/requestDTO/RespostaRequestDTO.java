package br.com.alura.ForumHub.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RespostaRequestDTO(
    @NotBlank
    String mensagem,

    @NotNull
    Long autorId,

    @NotNull
    Long topicoId
) {}
