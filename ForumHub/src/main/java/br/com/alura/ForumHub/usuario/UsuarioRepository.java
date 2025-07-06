package br.com.alura.ForumHub.usuario;

import br.com.alura.ForumHub.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
}
