package br.com.alura.ForumHub.domain.topico;

import br.com.alura.ForumHub.domain.curso.Curso;
import br.com.alura.ForumHub.domain.resposta.Resposta;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensagem;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusTopico status = StatusTopico.NAO_RESPONDIDO;

    @ManyToOne
    private Usuario autor;

    @ManyToOne
    private Curso curso;

    @OneToMany(mappedBy = "topico")
    private List<Resposta> respostas;
}
