package br.com.alura.ForumHub.domain.usuario;

import br.com.alura.ForumHub.domain.perfil.Perfil;
import br.com.alura.ForumHub.domain.resposta.Resposta;
import br.com.alura.ForumHub.domain.topico.Topico;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;

    @NotBlank
    @Email
    private String email;

    private String senha;

    @OneToMany(mappedBy = "autor")
    private List<Topico> topicos = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuarios_perfis",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfis;

    @OneToMany(mappedBy = "autor")
    private List<Resposta> respostas = new ArrayList<>();

    }
