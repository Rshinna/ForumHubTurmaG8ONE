package br.com.alura.ForumHub.repository;

import br.com.alura.ForumHub.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
}
