package br.com.alura.ForumHub.controller;

import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.dto.requestDTO.LoginRequestDTO;
import br.com.alura.ForumHub.dto.responseDTO.TokenResponseDTO;
import br.com.alura.ForumHub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenResponseDTO> autenticar(@RequestBody @Valid LoginRequestDTO dados) {
        var tokenAuth = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var autenticacao = authenticationManager.authenticate(tokenAuth);

        var usuario = (Usuario) autenticacao.getPrincipal();
        var jwt = tokenService.gerarToken(usuario);

        var dto = new TokenResponseDTO(jwt, "Bearer");
        return ResponseEntity.ok(dto);
    }
}
