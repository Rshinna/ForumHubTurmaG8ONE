package br.com.alura.ForumHub.infra.service;

import br.com.alura.ForumHub.domain.resposta.Resposta;
import br.com.alura.ForumHub.domain.topico.Topico;
import br.com.alura.ForumHub.domain.usuario.Usuario;
import br.com.alura.ForumHub.dto.requestDTO.RespostaRequestDTO;
import br.com.alura.ForumHub.dto.responseDTO.RespostaResponseDTO;
import br.com.alura.ForumHub.repository.RespostaRepository;
import br.com.alura.ForumHub.repository.TopicoRepository;
import br.com.alura.ForumHub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    public RespostaResponseDTO cadastrar(RespostaRequestDTO dto) {
        Resposta resposta = new Resposta();
        resposta.setMensagem(dto.mensagem());
        resposta.setAutor(buscarAutor(dto.autorId()));
        resposta.setTopico(buscarTopico(dto.topicoId()));

        respostaRepository.save(resposta);
        return toDTO(resposta);
    }

    public List<RespostaResponseDTO> listarTodas() {
        return respostaRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public RespostaResponseDTO detalhar(Long id) {
        Resposta resposta = buscarResposta(id);
        return toDTO(resposta);
    }

    public RespostaResponseDTO atualizar(Long id, RespostaRequestDTO dto) {
        Resposta resposta = buscarResposta(id);
        validarPermissao(resposta.getAutor());

        resposta.setMensagem(dto.mensagem());
        resposta.setAutor(buscarAutor(dto.autorId()));
        resposta.setTopico(buscarTopico(dto.topicoId()));

        respostaRepository.save(resposta);
        return toDTO(resposta);
    }

    public void excluir(Long id) {
        Resposta resposta = buscarResposta(id);
        validarPermissao(resposta.getAutor());
        respostaRepository.deleteById(id);
    }


    private Resposta buscarResposta(Long id) {
        return respostaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resposta com ID " + id + " não encontrada!"));
    }

    private Usuario buscarAutor(Long autorId) {
        return usuarioRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
    }

    private Topico buscarTopico(Long topicoId) {
        return topicoRepository.findById(topicoId)
                .orElseThrow(() -> new RuntimeException("Tópico não encontrado"));
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void validarPermissao(Usuario autorResposta) {
        Usuario usuarioLogado = getUsuarioLogado();
        boolean ehAdmin = usuarioLogado.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equals("ROLE_ADMIN"));

        if (!autorResposta.getId().equals(usuarioLogado.getId()) && !ehAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para modificar esta resposta.");
        }
    }

    private RespostaResponseDTO toDTO(Resposta resposta) {
        return new RespostaResponseDTO(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getAutor().getNome(),
                resposta.getTopico().getTitulo(),
                resposta.getDataCriacao(),
                resposta.getSolucao()
        );
    }
}
