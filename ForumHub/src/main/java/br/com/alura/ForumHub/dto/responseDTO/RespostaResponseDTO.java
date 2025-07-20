package br.com.alura.ForumHub.dto.responseDTO;

import java.time.LocalDateTime;

public record RespostaResponseDTO(

        Long id,
        String mensagem,
        String autor,
        String topico,
        LocalDateTime dataCriacao,
        Boolean solucao
) {}
