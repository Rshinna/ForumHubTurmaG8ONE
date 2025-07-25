package br.com.alura.ForumHub.infra.service;

import br.com.alura.ForumHub.domain.perfil.Perfil;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.dto.requestDTO.UsuarioRequestDTO;
import br.com.alura.ForumHub.repository.PerfilRepository;
import br.com.alura.ForumHub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PerfilRepository perfilRepository;

    public void cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(senhaCriptografada);

        Perfil perfilPadrao = perfilRepository.findByNome("ROLE_USER");
        if(perfilPadrao == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Perfil padrão ROLE_USER não encontrado!");
        }
        usuario.setPerfis(Set.of(perfilPadrao));

        usuarioRepository.save(usuario);
    }

    public void promoverParaAdmin(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        Perfil perfilAdmin = perfilRepository.findByNome("ROLE_ADMIN");
        if (perfilAdmin == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Perfil ROLE_ADMIN não encontrado.");
        }

        usuario.getPerfis().add(perfilAdmin);
        usuarioRepository.save(usuario);
    }

}

