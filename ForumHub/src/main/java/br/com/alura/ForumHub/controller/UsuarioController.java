package br.com.alura.ForumHub.controller;


import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.dto.requestDTO.UsuarioRequestDTO;
import br.com.alura.ForumHub.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByEmail(dto.email())) {
            return ResponseEntity.badRequest().body("Email já cadastrado.");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.senha());

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(senhaCriptografada);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso!");

    }
}
