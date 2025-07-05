package br.com.alura.ForumHub.domain.resposta;

import br.com.alura.ForumHub.domain.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;
    private String topico;
    private LocalDateTime dataCriacao;
    private Usuario autor;
    private String solucao;
}
