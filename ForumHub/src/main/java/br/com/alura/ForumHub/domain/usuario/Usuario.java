package br.com.alura.ForumHub.domain.usuario;

import br.com.alura.ForumHub.domain.perfil.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;

    @NotBlank
    @Email
    private String email;
    private String senha;


    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Perfil> perfis;

    public Usuario() {}
    public Usuario(Long id, String nome, String email, String senha, Set<Perfil> perfis) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfis = perfis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(Set<Perfil> perfis) {
        this.perfis = perfis;
    }
}
