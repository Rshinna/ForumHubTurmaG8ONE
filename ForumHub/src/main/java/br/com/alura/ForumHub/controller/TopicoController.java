package br.com.alura.ForumHub.controller;

import br.com.alura.ForumHub.repository.CursoRepository;
import br.com.alura.ForumHub.domain.curso.Curso;
import br.com.alura.ForumHub.domain.topico.Topico;
import br.com.alura.ForumHub.repository.TopicoRepository;
import br.com.alura.ForumHub.dto.requestDTO.TopicoRequestDTO;
import br.com.alura.ForumHub.dto.responseDTO.TopicoResponseDTO;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.infra.erros.ErroDTO;
import br.com.alura.ForumHub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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




}
