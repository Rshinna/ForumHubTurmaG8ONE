package br.com.alura.ForumHub.controller;

import br.com.alura.ForumHub.domain.resposta.Resposta;
import br.com.alura.ForumHub.domain.topico.Topico;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.dto.requestDTO.RespostaRequestDTO;
import br.com.alura.ForumHub.dto.responseDTO.RespostaResponseDTO;
import br.com.alura.ForumHub.repository.RespostaRepository;
import br.com.alura.ForumHub.repository.TopicoRepository;
import br.com.alura.ForumHub.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/respostas")
@SecurityRequirement(name = "bearer-key")
public class RespostaController {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity<RespostaResponseDTO> cadastrarResposta(@RequestBody @Valid RespostaRequestDTO dto) {
        Usuario autor = usuarioRepository.findById(dto.autorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Topico topico = topicoRepository.findById(dto.topicoId())
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));


       Resposta resposta = new Resposta();
       resposta.setMensagem(dto.mensagem());
       resposta.setAutor(autor);
       resposta.setTopico(topico);

       respostaRepository.save(resposta);

       RespostaResponseDTO response = new RespostaResponseDTO(
               resposta.getId(),
               resposta.getMensagem(),
               resposta.getAutor().getNome(),
               resposta.getTopico().getTitulo(),
               resposta.getDataCriacao(),
               resposta.getSolucao()
       );

       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RespostaResponseDTO>> listar() {
        List<Resposta> respostas = respostaRepository.findAll();

        List<RespostaResponseDTO> respostaDTOs = respostas.stream()
                .map(resposta -> new RespostaResponseDTO(
                        resposta.getId(),
                        resposta.getMensagem(),
                        resposta.getAutor().getNome(),
                        resposta.getTopico().getTitulo(),
                        resposta.getDataCriacao(),
                        resposta.getSolucao()
                ))
                .toList();

        return ResponseEntity.ok(respostaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable Long id) {
        Resposta resposta = respostaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resposta com ID " + id + " não encontrada!"));

        RespostaResponseDTO dto = new RespostaResponseDTO(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getAutor().getNome(),
                resposta.getTopico().getTitulo(),
                resposta.getDataCriacao(),
                resposta.getSolucao()
        );

        return ResponseEntity.ok(dto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid RespostaRequestDTO dto) {
        Optional<Resposta> respostaOptional = respostaRepository.findById(id);

        if(respostaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Resposta com ID " + id + " não encontrada!");
        }

        Resposta resposta = respostaOptional.get();

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        boolean ehAdmin = usuarioLogado.getAuthorities().stream()
                .anyMatch(p ->p.getAuthority().equals("ROLE_ADMIN"));

        if(!resposta.getAutor().getId().equals(usuarioLogado.getId()) && !ehAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para editar esta resposta!");
        }

        Usuario autor = usuarioRepository.findById(dto.autorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado!"));

        Topico topico = topicoRepository.findById(dto.topicoId())
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado!"));

        resposta.setMensagem(dto.mensagem());
        resposta.setAutor(autor);
        resposta.setTopico(topico);

        respostaRepository.save(resposta);

        RespostaResponseDTO respostaDTO = new RespostaResponseDTO(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getAutor().getNome(),
                resposta.getTopico().getTitulo(),
                resposta.getDataCriacao(),
                resposta.getSolucao()
        );

        return ResponseEntity.ok(respostaDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        Resposta resposta = respostaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Resposta com ID " + id + " não encontrada!"
                ));

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if(!resposta.getAutor().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para excluir esta resposta.");
        }

        respostaRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
