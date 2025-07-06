package br.com.alura.ForumHub.domain.topico;

import java.time.LocalDateTime;

public record TopicoResponseDTO(
        Long id,
        String titulo,
        String mensagem,
        String nomeCurso,
        String nomeAutor,
        LocalDateTime dataCriacao,
        String status
) {}
