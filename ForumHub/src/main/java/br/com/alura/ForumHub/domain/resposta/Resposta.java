package br.com.alura.ForumHub.domain.resposta;

import br.com.alura.ForumHub.domain.topico.Topico;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    @ManyToOne
    private Topico topico;

    @ManyToOne
    private Usuario autor;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    private Boolean solucao = false;

}
