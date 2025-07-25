package br.com.alura.ForumHub.controller;

import br.com.alura.ForumHub.dto.requestDTO.RespostaRequestDTO;
import br.com.alura.ForumHub.dto.responseDTO.RespostaResponseDTO;
import br.com.alura.ForumHub.infra.service.RespostaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/respostas")
@SecurityRequirement(name = "bearer-key")
public class RespostaController {

    @Autowired
    private RespostaService respostaService;

    @PostMapping
    public ResponseEntity<RespostaResponseDTO> cadastrar(@RequestBody @Valid RespostaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<RespostaResponseDTO>> listar() {
        return ResponseEntity.ok(respostaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaResponseDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(respostaService.detalhar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespostaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid RespostaRequestDTO dto) {
        return ResponseEntity.ok(respostaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        respostaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
