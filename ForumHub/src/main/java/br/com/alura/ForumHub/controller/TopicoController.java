package br.com.alura.ForumHub.controller;

import br.com.alura.ForumHub.domain.curso.Curso;
import br.com.alura.ForumHub.domain.topico.Topico;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.dto.requestDTO.TopicoRequestDTO;
import br.com.alura.ForumHub.dto.responseDTO.TopicoResponseDTO;
import br.com.alura.ForumHub.infra.erros.ErroDTO;
import br.com.alura.ForumHub.infra.service.TopicoService;
import br.com.alura.ForumHub.repository.CursoRepository;
import br.com.alura.ForumHub.repository.TopicoRepository;
import br.com.alura.ForumHub.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public ResponseEntity<TopicoResponseDTO> cadastrar(@RequestBody @Valid TopicoRequestDTO dto) {
        TopicoResponseDTO resposta = topicoService.cadastrar(dto);
        URI uri = URI.create("/topicos/" + resposta.id());
        return ResponseEntity.created(uri).body(resposta);
    }

    @GetMapping
    public ResponseEntity<Page<TopicoResponseDTO>> listar(@PageableDefault(size = 10, sort = "dataCriacao") Pageable paginacao) {
        return ResponseEntity.ok(topicoService.listar(paginacao));
    }

    @GetMapping("/recentes")
    public ResponseEntity<List<TopicoResponseDTO>> listarRecentes() {
        return ResponseEntity.ok(topicoService.listarRecentes());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TopicoResponseDTO>> buscarPorCursoEAno(@RequestParam String nomeCurso, @RequestParam int ano) {
        return ResponseEntity.ok(topicoService.buscarPorCursoEAno(nomeCurso, ano));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(topicoService.detalhar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid TopicoRequestDTO dto) {
        return ResponseEntity.ok(topicoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        topicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

