package br.com.alura.ForumHub.repository;

import br.com.alura.ForumHub.domain.topico.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico,Long> {
    boolean existsByTituloAndMensagem(String titulo, String mensagem);

    List<Topico> findTop10ByOrderByDataCriacaoAsc();

    @Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso AND FUNCTION('YEAR', t.dataCriacao) = :ano")
    List<Topico> buscarPorCursoEAno(@Param("nomeCurso") String nomeCurso, @Param("ano") int ano);
}
