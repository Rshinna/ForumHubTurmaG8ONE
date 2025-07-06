package br.com.alura.ForumHub.domain.curso;

import br.com.alura.ForumHub.domain.topico.Topico;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String categoria;

    @OneToMany(mappedBy = "curso")
    private List<Topico> topicos = new ArrayList<>();

}
