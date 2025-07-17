package br.com.alura.ForumHub.controller;

import br.com.alura.ForumHub.domain.curso.Curso;
import br.com.alura.ForumHub.domain.topico.Topico;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.dto.requestDTO.TopicoRequestDTO;
import br.com.alura.ForumHub.dto.responseDTO.TopicoResponseDTO;
import br.com.alura.ForumHub.infra.erros.ErroDTO;
import br.com.alura.ForumHub.repository.CursoRepository;
import br.com.alura.ForumHub.repository.TopicoRepository;
import br.com.alura.ForumHub.repository.UsuarioRepository;
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
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid TopicoRequestDTO dto) {
      if (topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem())) {
          return ResponseEntity.badRequest().body(new ErroDTO("Tópico já existe!"));
      }

      Curso curso = cursoRepository.findByNome(dto.nomeCurso());
      if(curso == null) {
          return ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body(new ErroDTO("Curso " + dto.nomeCurso() + " não encontrado!"));
      }

      Usuario autor = usuarioRepository.findById(dto.autorId())
              .orElseThrow(() -> new RuntimeException("Autor não encontrado!"));

      Topico topico = new Topico(dto.titulo(),dto.mensagem(),curso,autor);
      topicoRepository.save(topico);

      TopicoResponseDTO response = new TopicoResponseDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getCurso().getNome(),
                topico.getAutor().getNome(),
                topico.getDataCriacao(),
                topico.getStatus().name()
        );


        URI uri = URI.create("/topicos/" + topico.getId());
      return ResponseEntity.created(uri).body(response);

    }

    @GetMapping
    public ResponseEntity<Page<TopicoResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "dataCriacao")Pageable paginacao
            ) {
        Page<Topico> pagina = topicoRepository.findAll(paginacao);

        Page<TopicoResponseDTO> resposta = pagina.map(topico -> new TopicoResponseDTO(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensagem(),
                        topico.getCurso().getNome(),
                        topico.getAutor().getNome(),
                        topico.getDataCriacao(),
                        topico.getStatus().name()
                ));

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/recentes")
    public ResponseEntity<List<TopicoResponseDTO>> listarRecentes() {
        List<Topico> topicos = topicoRepository.findTop10ByOrderByDataCriacaoAsc();
        List<TopicoResponseDTO> resposta = topicos.stream()
                .map(topico -> new TopicoResponseDTO(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensagem(),
                        topico.getCurso().getNome(),
                        topico.getAutor().getNome(),
                        topico.getDataCriacao(),
                        topico.getStatus().name()
                )).toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TopicoResponseDTO>> buscarPorCursoEAno(
            @RequestParam String nomeCurso,
            @RequestParam int ano
    ) {
        List<Topico> topicos = topicoRepository.buscarPorCursoEAno(nomeCurso, ano);
        List<TopicoResponseDTO> resposta = topicos.stream()
                .map(topico -> new TopicoResponseDTO(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensagem(),
                        topico.getCurso().getNome(),
                        topico.getAutor().getNome(),
                        topico.getDataCriacao(),
                        topico.getStatus().name()
                )).toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico com ID " + id + " não encontrado!"));

        TopicoResponseDTO resposta = new TopicoResponseDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getCurso().getNome(),
                topico.getAutor().getNome(),
                topico.getDataCriacao(),
                topico.getStatus().name()
        );

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid TopicoRequestDTO dto){
        Optional<Topico> topicoOptional = topicoRepository.findById(id);

     if(topicoOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroDTO("Tópico com ID " + " não encontrado!"));
     }

     Topico topico = topicoOptional.get();
     if(topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem()) && (!topico.getTitulo().equals(dto.titulo()) || !topico.getMensagem().equals(dto.mensagem()))) {
       return ResponseEntity.badRequest().body(new ErroDTO("Já existe outro tópico com esse título e mensagem!"));
     }

     Curso curso = cursoRepository.findByNome(dto.nomeCurso());
     Usuario autor = usuarioRepository.findById(dto.autorId())
             .orElseThrow(() -> new RuntimeException("Autor não encontrado!"));
        topico.setTitulo(dto.titulo());
        topico.setMensagem(dto.mensagem());
        topico.setCurso(curso);
        topico.setAutor(autor);

        topicoRepository.save(topico);

        TopicoResponseDTO resposta = new TopicoResponseDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getCurso().getNome(),
                topico.getAutor().getNome(),
                topico.getDataCriacao(),
                topico.getStatus().name()
        );

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        Optional<Topico> topicoOptional = topicoRepository.findById(id);

        if(topicoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErroDTO("Tópico com ID " + id + " não encontrado"));
        }

        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
