package br.com.alura.ForumHub.controller;


import br.com.alura.ForumHub.dto.requestDTO.UsuarioRequestDTO;
import br.com.alura.ForumHub.infra.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        usuarioService.cadastrar(dto);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @PatchMapping("/{id}/promover")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> promoverParaAdmin(@PathVariable Long id) {
        usuarioService.promoverParaAdmin(id);
        return ResponseEntity.ok("Usuário promovido para ADMIN com sucesso!");
    }

}
