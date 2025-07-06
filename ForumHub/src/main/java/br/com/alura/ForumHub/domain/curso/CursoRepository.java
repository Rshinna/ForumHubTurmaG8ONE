package br.com.alura.ForumHub.domain.curso;

import jakarta.validation.constraints.NotBlank;

public interface CursoRepository {
    Curso findByNome(@NotBlank String s);
}
