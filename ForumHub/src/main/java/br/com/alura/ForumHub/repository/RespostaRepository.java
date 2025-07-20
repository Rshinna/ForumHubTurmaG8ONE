package br.com.alura.ForumHub.repository;

import br.com.alura.ForumHub.domain.resposta.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {
}
