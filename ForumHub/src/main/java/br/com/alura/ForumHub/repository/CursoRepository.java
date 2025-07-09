package br.com.alura.ForumHub.repository;

import br.com.alura.ForumHub.domain.curso.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository  extends JpaRepository<Curso, Long> {
    Curso findByNome(String nome);
}
