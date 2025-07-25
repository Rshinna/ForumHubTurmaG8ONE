package br.com.alura.ForumHub.infra.service;

import br.com.alura.ForumHub.domain.curso.Curso;
import br.com.alura.ForumHub.domain.topico.Topico;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.dto.requestDTO.TopicoRequestDTO;
import br.com.alura.ForumHub.dto.responseDTO.TopicoResponseDTO;
import br.com.alura.ForumHub.repository.CursoRepository;
import br.com.alura.ForumHub.repository.TopicoRepository;
import br.com.alura.ForumHub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public TopicoResponseDTO cadastrar(TopicoRequestDTO dto) {
        if (topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tópico já existe!");
        }

        Curso curso = buscarCurso(dto.nomeCurso());
        Usuario autor = buscarAutor(dto.autorId());

        Topico topico = new Topico(dto.titulo(), dto.mensagem(), curso, autor);
        topicoRepository.save(topico);

        return toDTO(topico);
    }

    public Page<TopicoResponseDTO> listar(Pageable paginacao) {
        return topicoRepository.findAll(paginacao).map(this::toDTO);
    }

    public List<TopicoResponseDTO> listarRecentes() {
        return topicoRepository.findTop10ByOrderByDataCriacaoAsc().stream()
                .map(this::toDTO)
                .toList();
    }

    public List<TopicoResponseDTO> buscarPorCursoEAno(String nomeCurso, int ano) {
        return topicoRepository.buscarPorCursoEAno(nomeCurso, ano).stream()
                .map(this::toDTO)
                .toList();
    }

    public TopicoResponseDTO detalhar(Long id) {
        Topico topico = buscarTopico(id);
        return toDTO(topico);
    }

    public TopicoResponseDTO atualizar(Long id, TopicoRequestDTO dto) {
        Topico topico = buscarTopico(id);

        boolean tituloMudou = !topico.getTitulo().equals(dto.titulo());
        boolean mensagemMudou = !topico.getMensagem().equals(dto.mensagem());

        if (topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem()) && (tituloMudou || mensagemMudou)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe outro tópico com esse título e mensagem!");
        }

        Curso curso = buscarCurso(dto.nomeCurso());
        Usuario autor = buscarAutor(dto.autorId());

        topico.setTitulo(dto.titulo());
        topico.setMensagem(dto.mensagem());
        topico.setCurso(curso);
        topico.setAutor(autor);

        topicoRepository.save(topico);
        return toDTO(topico);
    }

    public void excluir(Long id) {
        Topico topico = buscarTopico(id);
        topicoRepository.delete(topico);
    }


    private Topico buscarTopico(Long id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico com ID " + id + " não encontrado!"));
    }

    private Curso buscarCurso(String nomeCurso) {
        Curso curso = cursoRepository.findByNome(nomeCurso);
        if (curso == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso " + nomeCurso + " não encontrado!");
        }
        return curso;
    }

    private Usuario buscarAutor(Long autorId) {
        return usuarioRepository.findById(autorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado!"));
    }

    private TopicoResponseDTO toDTO(Topico topico) {
        return new TopicoResponseDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getCurso().getNome(),
                topico.getAutor().getNome(),
                topico.getDataCriacao(),
                topico.getStatus().name()
        );
    }
}

