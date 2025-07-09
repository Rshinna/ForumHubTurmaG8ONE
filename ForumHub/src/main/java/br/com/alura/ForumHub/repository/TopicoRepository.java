package br.com.alura.ForumHub.repository;

import br.com.alura.ForumHub.domain.topico.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico,Long> {
    boolean existsByTituloAndMensagem(String titulo, String mensagem);
}
